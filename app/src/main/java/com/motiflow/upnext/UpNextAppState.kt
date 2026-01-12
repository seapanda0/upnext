package com.motiflow.upnext

import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController

// Wrapper for navcontroller object
@Stable
class UpNextAppState (val navController: NavHostController){
    fun popUp(){
        navController.popBackStack()
    }
    fun navigateTo(route: String){
        navController.navigate(route){ launchSingleTop = true}
    }
    fun navigateAndPopUp(route: String, popUp: String){
        navController.navigate(route){
            launchSingleTop = true
            popUpTo (popUp){ inclusive = true }
        }
    }
    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }

}