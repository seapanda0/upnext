package com.motiflow.upnext.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.motiflow.upnext.Routes
import com.motiflow.upnext.model.AccountService
import com.motiflow.upnext.AccountType
import com.motiflow.upnext.User
import com.motiflow.upnext.model.DataRepoService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RegisterViewModel() : ViewModel(){

    val registeringUser = MutableStateFlow<User>(User())
    val password = MutableStateFlow("")
    fun updatePassword(newPassword: String){
        password.value = newPassword
    }
    fun updateEmail(newEmail: String){
        registeringUser.value = registeringUser.value.copy(email = newEmail)
    }
    fun updateAccountType(newAccType: AccountType){
        registeringUser.value = registeringUser.value.copy(accountType = newAccType)
    }
    fun updateUserName(newUserName: String){
        registeringUser.value = registeringUser.value.copy(username = newUserName)
    }
    fun switchToLogin(navigateTo: (String) -> Unit){
        navigateTo(Routes.LOGIN_SCREEN)
    }
    fun onRegisterClick(openAndPopUp: (String, String) -> Unit){
        viewModelScope.launch {
            try{
                // Sign up for the user
                val result = AccountService.signUp(registeringUser.value.email.trim(), password.value.trim())

                // Check if UID created
                val uid = result.user?.uid ?: throw IllegalStateException("UID is null after registraion")

                // Modify data before sending to firestore
                registeringUser.value.uid = uid
                registeringUser.value.createdAt = Timestamp.now()

                DataRepoService.addUser(registeringUser.value)

                val user = DataRepoService.currentUser.first()
                when(registeringUser.value.accountType){
                    AccountType.MANAGER -> {
                        openAndPopUp(Routes.MANAGER_WORKER_LIST_SCREEN, Routes.REGISTER_SCREEN)
                    }
                    AccountType.WORKER -> {
                        openAndPopUp(Routes.WORKER_TODO_LIST_SCREEN, Routes.REGISTER_SCREEN)
                    }
                    null -> {
                        openAndPopUp(Routes.SPLASH_SCREEN, Routes.REGISTER_SCREEN)
                    }
                }
            } catch (e: FirebaseAuthWeakPasswordException) {

            } catch (e: FirebaseAuthInvalidCredentialsException){

            } catch (e: FirebaseAuthUserCollisionException){

            }catch (e: IllegalArgumentException){

            }

        }
    }
}