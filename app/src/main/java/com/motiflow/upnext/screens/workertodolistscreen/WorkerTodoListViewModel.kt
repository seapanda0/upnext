package com.motiflow.upnext.screens.workertodolistscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motiflow.upnext.AccountType
import com.motiflow.upnext.Routes
import com.motiflow.upnext.Todo
import com.motiflow.upnext.model.DataRepoService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkerTodoListViewModel() : ViewModel() {
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos
    // Select what data to show depending on worker or manager
    fun initialize(workerUid: String){
        viewModelScope.launch {
            val flow = if (workerUid == "-1"){
                DataRepoService.todos
            }else{
                DataRepoService.todosForWorker(workerUid)
            }
            flow.collect { list -> _todos.value = list }
        }
    }
    fun onClickTodo(navigateTo: (String) -> Unit, todoId: String){
        // Log.d("Worker todolist VM","Clicked")
        // concatnate the todoID here
        navigateTo(Routes.EDIT_TODO_SCREEN+"?todoId=${todoId}")
    }
    fun addnewTodo(navigateTo: (String) -> Unit){
        navigateTo(Routes.EDIT_TODO_SCREEN+"?todoId=-1")
    }
    fun onDeleteTodo(todoId: String){
        viewModelScope.launch {
            DataRepoService.deleteTodo(todoId)
        }
    }
//    val dummyTestTodo = Todo();
//    fun onDummyAddTodoClick(){
//        viewModelScope.launch {
//            Log.d("WORKER TODO VM", "BUTTON PRESSED")
//            DataRepoService.workerCreateTodo(dummyTestTodo)
//        }
//    }
}