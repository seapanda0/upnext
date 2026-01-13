package com.motiflow.upnext.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    openAndPopUp: (String, String) -> Unit,
    navigateTo: (String) -> Unit
) {
    val email = viewModel.email.collectAsState()
    val password = viewModel.password.collectAsState()

    Scaffold{ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Sign in to UpNext",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = email.value,
                onValueChange = {viewModel.updateEmail(it)},
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // ---- Password ----
            OutlinedTextField(
                value = password.value,
                onValueChange = {viewModel.updatePassword(it)},
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            // ---- Login Button (Primary Action) ----
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.onLoginClick(openAndPopUp) }
            ) {
                Text("Login")
            }

            Spacer(Modifier.height(16.dp))

            // Register button
            TextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { viewModel.switchToRegister(navigateTo) }
            ) {
                Text("Don’t have an account? Register")
            }
        }
    }
}
