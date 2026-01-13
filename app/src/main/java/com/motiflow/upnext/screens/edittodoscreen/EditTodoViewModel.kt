package com.motiflow.upnext.screens.edittodoscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motiflow.upnext.Routes
import com.motiflow.upnext.Todo
import com.motiflow.upnext.model.AccountService
import com.motiflow.upnext.model.DataRepoService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EditTodoViewModel() : ViewModel() {
    val todo = MutableStateFlow<Todo>(DEFAULT_TODO)
    var _isNewTodo = true
    var _assignedToId = "-1"

    fun initialize (todoId: String, assignedToId : String ,restartApp: (String) -> Unit){
        viewModelScope.launch {
            if (todoId == "-1"){
                _isNewTodo = true
                _assignedToId = assignedToId
                todo.value = DEFAULT_TODO

            }else{
                _isNewTodo = false
                todo.value = DataRepoService.readTodo(todoId)?: DEFAULT_TODO
            }
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
            if (_isNewTodo){
                if(_assignedToId == "-1"){
                    // If this is worker adding todo, datarepo will auto add his uid
                    DataRepoService.workerCreateTodo(todo.value)
                }else{
                    // If this is manager adding todo, assign workerid
                    todo.value = todo.value.copy(
                        assignedToUid = _assignedToId
                    )
                    DataRepoService.managerCreateTodo(todo.value)
                }
            }else {
                // Updating procedure for both worker and manager is the same
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