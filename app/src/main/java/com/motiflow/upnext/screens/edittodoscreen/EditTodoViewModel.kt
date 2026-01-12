package com.motiflow.upnext.screens.edittodoscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.motiflow.upnext.Routes
import com.motiflow.upnext.Todo
import com.motiflow.upnext.model.AccountService
import com.motiflow.upnext.model.DataRepoService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EditTodoViewModel() : ViewModel() {
    val todo = MutableStateFlow<Todo>(DEFAULT_TODO)

    fun initialize (todoId: String, restartApp: (String) -> Unit){
        viewModelScope.launch {
            todo.value = DataRepoService.readTodo(todoId)?: DEFAULT_TODO
        }
        observeAuthenticationState(restartApp)
    }

    private fun observeAuthenticationState(restartApp: (String) -> Unit){
        viewModelScope.launch {
            AccountService.currentUser.collect { user ->
                if (user == null) restartApp(Routes.SPLASH_SCREEN)
            }
        }
    }
    fun saveTodo(popUpScreen:() -> Unit){
        viewModelScope.launch {
            if (todo.value.id == "-1"){
                // Unsure, placeholder for now
                DataRepoService.workerCreateTodo(todo.value)
            }else {
                DataRepoService.updateTodo(todo.value)
            }
            popUpScreen()
        }
    }
    fun onCancelEdit(popUpScreen: () -> Unit){
        popUpScreen()
    }

    companion object{
        private val DEFAULT_TODO = Todo()
    }
}