package com.yoyopab.todo.detail

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yoyopab.todo.detail.ui.theme.TodoYohanPabloTheme
import com.yoyopab.todo.list.Task
import java.util.UUID

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoYohanPabloTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Detail(
                        modifier = Modifier.padding(innerPadding),
                        onValidate = { newTask ->
                            intent.putExtra("task", newTask)
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Detail(modifier: Modifier = Modifier, onValidate: (Task) -> Unit) {
    var task by remember { mutableStateOf(Task(id = UUID.randomUUID().toString(), title = "New Task !")) }
    var textTitle by remember { mutableStateOf("") }
    var textDescr by remember { mutableStateOf("") }
    Column (
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Task detail",
            modifier = modifier,
            style = MaterialTheme.typography.headlineLarge
        )
        OutlinedTextField(
            value = textTitle,
            label = {Text("Title")},
            onValueChange = { textTitle = it }
        )
        OutlinedTextField(
            value = textDescr,
            label = {Text("Description")},
            onValueChange = { textDescr = it }
        )
        Button(
            onClick = {
                val newTask = Task(id = UUID.randomUUID().toString(), title = textTitle, description = textDescr)
                onValidate(newTask)
            }
        ){
            Text(
                text = "Send",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    TodoYohanPabloTheme {
        Detail(
            onValidate = {}
        )
    }
}