package com.motiflow.upnext.screens.workertodolistscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motiflow.upnext.Routes
import com.motiflow.upnext.Todo
import com.motiflow.upnext.model.DataRepoService
import kotlinx.coroutines.launch

class WorkerTodoListViewModel() : ViewModel() {
    val todos = DataRepoService.todos
    val dummyTestTodo = Todo();

    fun onClickTodo(navigateTo: (String) -> Unit, todoId: String){
        // Log.d("Worker todolist VM","Clicked")
        // concatnate the todoID here
        navigateTo(Routes.EDIT_TODO_SCREEN+"?todoId=${todoId}")
    }
    fun onDummyAddTodoClick(){
        viewModelScope.launch {
            Log.d("WORKER TODO VM", "BUTTON PRESSED")
            DataRepoService.workerAddTodo(dummyTestTodo)
        }
    }
}