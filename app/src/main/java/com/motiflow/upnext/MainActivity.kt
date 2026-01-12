package com.motiflow.upnext

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.motiflow.upnext.screens.addnewtodo.AddNewTodoScreen
import com.motiflow.upnext.screens.login.LoginScreen
import com.motiflow.upnext.screens.register.RegisterScreen
import com.motiflow.upnext.screens.splashscreen.SplashScreen
import com.motiflow.upnext.screens.workertodolistscreen.WorkerTodoListScreen
import com.motiflow.upnext.ui.theme.UpNextTheme

const val USE_EMULATOR = true
const val LOCALHOST = "192.168.100.164"
const val AUTH_PORT = 9099
const val FIRESTORE_PORT = 8080

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureFirebaseServices()
        enableEdgeToEdge()
        setContent {
            UpNextTheme {
                val navController = rememberNavController()
                val UpNextAppState = UpNextAppState(navController)
                NavHost(
                    navController = navController,
                    startDestination = Routes.SPLASH_SCREEN
                ){
                    composable(
                        route = Routes.SPLASH_SCREEN
                    ) {
                        SplashScreen(openAndPopUp = {route, popUp -> UpNextAppState.navigateAndPopUp(route, popUp)})
                    }
                    composable(
                        route = Routes.LOGIN_SCREEN
                    ){
                        LoginScreen(
                            openAndPopUp = {route, popUp -> UpNextAppState.navigateAndPopUp(route, popUp)},
                            navigateTo = {route -> UpNextAppState.navigateTo(route)},
                        )
                    }
                    composable(
                        route = Routes.REGISTER_SCREEN
                    ){
                        RegisterScreen(
                            openAndPopUp = {route, popUp -> UpNextAppState.navigateAndPopUp(route, popUp)},
                            navigateTo = {route -> UpNextAppState.navigateTo(route)},
                        )
                    }
                    composable(
                        route = Routes.WORKER_TODO_LIST_SCREEN
                    ){
                        WorkerTodoListScreen(navigateTo = {route -> UpNextAppState.navigateTo(route)})
                    }
                    composable (
                        route = Routes.ADD_NEW_TODO_SCREEN
                    ){ AddNewTodoScreen() }

                    composable (
                        route = Routes.EDIT_TODO_SCREEN + "?todoId={todoId}",
                        arguments = listOf(navArgument(name = "todoId"){
                            type = NavType.StringType
                            defaultValue = "-1"
                        })
                    ){ AddNewTodoScreen() }
                }
            }
        }
    }
    private fun configureFirebaseServices(){
        if(USE_EMULATOR){
            Firebase.auth.useEmulator(LOCALHOST, AUTH_PORT)
            Firebase.firestore.useEmulator(LOCALHOST, FIRESTORE_PORT)
        }
    }
}