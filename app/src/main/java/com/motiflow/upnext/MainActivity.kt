package com.motiflow.upnext

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.initialize
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
                        WorkerTodoListScreen()
                    }

                }
            }
        }
    }
    private fun configureFirebaseServices(){
        if(USE_EMULATOR){
            Firebase.auth.useEmulator(LOCALHOST, AUTH_PORT)
        }
    }
}