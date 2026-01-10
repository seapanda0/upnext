package com.motiflow.upnext

import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController

@Stable
class UpNextAppState (val navController: NavHostController){
    fun popOp(){
        navController.popBackStack()
    }
    fun navigateAndPopUp(route: String, popUp: String){
        navController.navigate(route){
            launchSingleTop = true
            popUpTo (popUp){ inclusive = true }
        }
    }

}