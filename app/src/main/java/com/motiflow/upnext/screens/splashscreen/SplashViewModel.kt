package com.motiflow.upnext.screens.splashscreen

import com.motiflow.upnext.Routes
import com.motiflow.upnext.model.AccountService

class SplashViewModel (){
    fun onAppStart(openAndPopUp : (String, String) -> Unit){
        if (AccountService.hasUser()) {
            openAndPopUp(Routes.WORKER_TODO_LIST_SCREEN, Routes.SPLASH_SCREEN)
        }
        else{
            openAndPopUp(Routes.LOGIN_SCREEN, Routes.SPLASH_SCREEN)
        }
    }
}