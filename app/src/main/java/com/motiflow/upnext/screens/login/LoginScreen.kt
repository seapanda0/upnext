package com.motiflow.upnext.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.motiflow.upnext.Routes

@Composable
fun LoginScreen(
    viewModel : LoginViewModel = viewModel(),
    openAndPopUp : (String, String) -> Unit,
    navigateTo : (String) -> Unit
){
    val email = viewModel.email.collectAsState()
    val password = viewModel.password.collectAsState()

    Column() {
        Text(
            text = Routes.LOGIN_SCREEN,
            fontSize = 40.sp
        )
        OutlinedTextField(
            value = email.value,
            onValueChange = {viewModel.updateEmail(it)},
            placeholder = { Text("Email")}
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = {viewModel.updatePassword(it)},
            placeholder = {Text("Password")}
        )
        Button(
            onClick = {viewModel.onLoginClick(openAndPopUp)},
        ) {
            Text("Login")
        }
        Button(
            onClick = {viewModel.switchToRegister(navigateTo)}
        ) {
            Text("Register Instead")
        }

    }
}