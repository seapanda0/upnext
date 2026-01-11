package com.motiflow.upnext.screens.workertodolistscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.motiflow.upnext.Todo
import com.motiflow.upnext.model.DataRepoService

@Composable
fun WorkerTodoListScreen(
    viewModel: WorkerTodoListViewModel = viewModel()
){
    val todos = viewModel.todos.collectAsState(initial = emptyList())
    Scaffold() { innerPadding ->
        Column() {
            Text(
                text = "Workertodolistscreen",
                fontSize = 40.sp
            )
            Button(
                onClick = {viewModel.onDummyAddTodoClick()}
            ) { Text("ADD DUMMY TODO")}

            LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                items(todos.value){ todo ->
                    TodoItem(todo)
                }
            }
        }
    }
}

@Composable
fun TodoItem(todo : Todo){
    Row() {
        Column() {
            Row() {
                Text(todo.title!!)
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = {Unit}) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
            todo.description?.let{
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it)
            }
        }
    }

}