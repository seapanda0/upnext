package com.motiflow.upnext.screens.register
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.motiflow.upnext.Routes
import com.motiflow.upnext.AccountType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    openAndPopUp: (String, String) -> Unit,
    navigateTo: (String) -> Unit
) {
    val registeringUser = viewModel.registeringUser.collectAsState()
    val password = viewModel.password.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Create account",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Join us and get started",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = registeringUser.value.email,
            onValueChange = { viewModel.updateEmail(it) },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = registeringUser.value.username,
            onValueChange = { viewModel.updateUserName(it) },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Account type",
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = registeringUser.value.accountType == AccountType.WORKER,
                    onClick = { viewModel.updateAccountType(AccountType.WORKER) }
                )
                Text("Worker")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = registeringUser.value.accountType == AccountType.MANAGER,
                    onClick = { viewModel.updateAccountType(AccountType.MANAGER) }
                )
                Text("Manager")
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { viewModel.onRegisterClick(openAndPopUp) }
        ) {
            Text("Register")
        }

        Spacer(Modifier.height(12.dp))

        TextButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { viewModel.switchToLogin(navigateTo) }
        ) {
            Text("Already have an account? Login")
        }
    }
}