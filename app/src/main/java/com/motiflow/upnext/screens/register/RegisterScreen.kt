package com.motiflow.upnext.screens.register

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(
    viewModel : LoginViewModel = viewModel(),
    openAndPopUp : (String, String) -> Unit
){
    val email = viewModel.email.collectAsState()
    val password = viewModel.password.collectAsState()

    Column() {
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
    }
}