package com.motiflow.upnext.screens.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motiflow.upnext.AccountType
import com.motiflow.upnext.Routes
import com.motiflow.upnext.model.AccountService
import com.motiflow.upnext.model.DataRepoService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel(){
    fun onAppStart(openAndPopUp : (String, String) -> Unit){
        if (!AccountService.hasUser()) {
            openAndPopUp(Routes.LOGIN_SCREEN, Routes.SPLASH_SCREEN)
            return
        }
        viewModelScope.launch {
            val user = DataRepoService.currentUser.first()
            when(user?.accountType){
                AccountType.MANAGER -> {
                    openAndPopUp(Routes.MANAGER_WORKER_LIST_SCREEN, Routes.SPLASH_SCREEN)
                }
                AccountType.WORKER -> {
                    openAndPopUp(Routes.WORKER_TODO_LIST_SCREEN, Routes.SPLASH_SCREEN)
                }
                null -> {
                    openAndPopUp(Routes.LOGIN_SCREEN, Routes.SPLASH_SCREEN)
                }
            }
        }
    }
}