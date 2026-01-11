package com.motiflow.upnext.screens.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.motiflow.upnext.Routes
import com.motiflow.upnext.AccountType

@Composable
fun RegisterScreen(
    viewModel : RegisterViewModel = viewModel(),
    openAndPopUp : (String, String) -> Unit,
    navigateTo : (String) -> Unit
){
    val registeringUser = viewModel.registeringUser.collectAsState()
    val password = viewModel.password.collectAsState()
    Column() {
        Text(
            text = Routes.REGISTER_SCREEN,
            fontSize = 40.sp
        )
        OutlinedTextField(
            value = registeringUser.value.email,
            onValueChange = {viewModel.updateEmail(it)},
            placeholder = { Text("Email")}
        )
        OutlinedTextField(
            value = registeringUser.value.username,
            onValueChange = {viewModel.updateUserName(it)},
            placeholder = { Text("Username")}
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = {viewModel.updatePassword(it)},
            placeholder = {Text("Password")}
        )
        Row(){
            RadioButton(
                selected = (registeringUser.value.accountType == AccountType.WORKER),
                onClick = {viewModel.updateAccountType((AccountType.WORKER))}
            )
            Text("Worker")
        }
        Row(){
            RadioButton(
                selected = (registeringUser.value.accountType == AccountType.MANAGER),
                onClick = {viewModel.updateAccountType((AccountType.MANAGER))}
            )
            Text("Manager")
        }
        Button(
            onClick = {viewModel.onRegisterClick(openAndPopUp)},
        ) {
            Text("Register")
        }

        Button(
            onClick = {viewModel.switchToLogin(navigateTo)},
        ) {
            Text("Login Instead")
        }

    }
}