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
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.motiflow.upnext.ui.theme.UpNextTheme

const val USE_EMULATOR = true
const val LOCALHOST = "192.168.100.164"
const val AUTH_PORT = 9099
const val FIRESTORE_PORT = 8080

object Routes {
    const val SPLASH_SCREEN = "splash_screen"
    const val LOGIN_SCREEN = "login_screen"
    const val REGISTER_SCREEN = "register_screen"
    const val WORKER_TODO_LIST_SCREEN = "worker_todo_list_screen"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureFirebaseServices()
        enableEdgeToEdge()
        setContent {
            UpNextTheme {
                val navController = rememberNavController()
//                NavHost(
//                    navController = navController,
//                    startDestination =
//
//                ){
//
//                }
            }
        }
    }
    private fun configureFirebaseServices(){
        if(USE_EMULATOR){
            Firebase.auth.useEmulator(LOCALHOST, AUTH_PORT)
        }
    }
}