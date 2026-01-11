package com.motiflow.upnext.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.motiflow.upnext.Routes
import com.motiflow.upnext.model.AccountService
import com.motiflow.upnext.model.AccountType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel() : ViewModel(){
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    val account_type = MutableStateFlow<AccountType>(AccountType.WORKER)

    fun updateEmail(newEmail: String){
        email.value = newEmail
    }
    fun updatePassword(newPassword: String){
        password.value = newPassword
    }
    fun updateAccountType(newAccType: AccountType){
        account_type.value = newAccType
    }

    fun switchToLogin(navigateTo: (String) -> Unit){
        navigateTo(Routes.LOGIN_SCREEN)
    }

    fun onRegisterClick(openAndPopUp: (String, String) -> Unit){
        viewModelScope.launch {
            try{
                AccountService.signUp(email.value, password.value)
                openAndPopUp(Routes.WORKER_TODO_LIST_SCREEN, Routes.REGISTER_SCREEN)
            } catch (e: FirebaseAuthWeakPasswordException) {

            } catch (e: FirebaseAuthInvalidCredentialsException){

            } catch (e: FirebaseAuthUserCollisionException){

            }catch (e: IllegalArgumentException){

            }

        }
    }
}