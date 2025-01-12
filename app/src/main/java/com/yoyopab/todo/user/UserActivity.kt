package com.yoyopab.todo.user

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.Manifest
import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import com.yoyopab.todo.R
import com.yoyopab.todo.data.Api
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var bitmap: Bitmap? by remember { mutableStateOf(null) }
            var uri: Uri? by remember { mutableStateOf(null) }

            val coroutineScope = rememberCoroutineScope()

            val captureUri by lazy {
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
            }

            val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { succes ->
                if (succes) {
                    uri = captureUri
                    coroutineScope.launch {
                        try {
                            val avatarPart = captureUri?.toRequestBody(this@UserActivity)
                            val response = Api.userWebService.updateAvatar(avatarPart!!)
                            if (response.isSuccessful) {
                                // Succès
                            } else {
                                // Gérer l'erreur
                            }
                        } catch (e: Exception) {
                            // Gérer les exceptions réseau
                        }
                    }
                }
            }

            val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { selectedUri ->
                if (selectedUri != null) {
                    uri = selectedUri
                    coroutineScope.launch {
                        try {
                            val avatarPart = selectedUri.toRequestBody(this@UserActivity)
                            val response = Api.userWebService.updateAvatar(avatarPart)
                            if (response.isSuccessful) {
                                // Succès
                            } else {
                                // Gérer l'erreur
                            }
                        } catch (e: Exception) {
                            // Gérer les exceptions réseau
                        }
                    }
                }
            }

            val requestedPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                }
            }

            Column {
                AsyncImage(
                    modifier = Modifier.fillMaxHeight(.2f),
                    model = bitmap ?: uri,
                    contentDescription = null
                )
                Button(
                    onClick = { takePicture.launch(captureUri!!) },
                    content = { Text("Take picture") }
                )
                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= 29){
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                        } else {
                            requestedPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    },
                    content = { Text("Pick photo") }
                )
            }
        }
    }
}

private fun Bitmap.toRequestBody(): MultipartBody.Part {
    val tmpFile = File.createTempFile("avatar", "jpg")
    tmpFile.outputStream().use { // *use*: open et close automatiquement
        this.compress(Bitmap.CompressFormat.JPEG, 100, it) // *this* est le bitmap ici
    }
    return MultipartBody.Part.createFormData(
        name = "avatar",
        filename = "avatar.jpg",
        body = tmpFile.readBytes().toRequestBody()
    )
}

private fun Uri.toRequestBody(context: Context): MultipartBody.Part {
    val fileInputStream = context.contentResolver.openInputStream(this)!!
    val fileBody = fileInputStream.readBytes().toRequestBody()
    return MultipartBody.Part.createFormData(
        name = "avatar",
        filename = "avatar.jpg",
        body = fileBody
    )
}
