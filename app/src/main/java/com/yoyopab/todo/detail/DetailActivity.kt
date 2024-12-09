package com.yoyopab.todo.detail

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yoyopab.todo.detail.ui.theme.TodoYohanPabloTheme

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoYohanPabloTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Detail(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Detail(modifier: Modifier = Modifier) {
    Column (
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Task detail",
            modifier = modifier,
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "title",
            modifier = modifier
        )
        Text(
            text = "description",
            modifier = modifier
        )
        Button(
            onClick = {},
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
        Detail()
    }
}