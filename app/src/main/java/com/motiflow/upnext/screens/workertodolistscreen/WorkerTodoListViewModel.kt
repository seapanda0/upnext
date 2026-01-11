package com.motiflow.upnext.screens.workertodolistscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motiflow.upnext.Todo
import com.motiflow.upnext.model.DataRepoService
import kotlinx.coroutines.launch

class WorkerTodoListViewModel() : ViewModel() {
    val todos = DataRepoService.todos
    val dummyTestTodo = Todo();

    fun onDummyAddTodoClick(){
        viewModelScope.launch {
            Log.d("WORKER TODO VM", "BUTTON PRESSED")
            DataRepoService.workerAddTodo(dummyTestTodo)
        }
    }
}