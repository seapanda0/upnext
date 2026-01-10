package com.motiflow.upnext.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.motiflow.upnext.Routes
import com.motiflow.upnext.model.AccountService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel(){
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    fun updateEmail(newEmail: String){
        email.value = newEmail
    }
    fun updatePassword(newPassword: String){
        password.value = newPassword
    }
    fun onLoginClick(openAndPopUp: (String, String) -> Unit){
        viewModelScope.launch {
            try{
                AccountService.signIn(email.value, password.value)
                openAndPopUp(Routes.WORKER_TODO_LIST_SCREEN, Routes.LOGIN_SCREEN)
            } catch (e: FirebaseAuthInvalidUserException) {

            } catch (e: FirebaseAuthInvalidCredentialsException){

            } catch (e: FirebaseAuthInvalidCredentialsException){

            }
        }
    }

}