package com.jsborbon.reparalo.screens.authentication.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.utils.ValidationUtils.areNotEmpty
import com.jsborbon.reparalo.utils.ValidationUtils.isStrongPassword
import com.jsborbon.reparalo.utils.ValidationUtils.isValidEmail
import com.jsborbon.reparalo.viewmodels.AuthViewModel

@Composable
fun LoginForm(navController: NavController, viewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val loginState by viewModel.loginState.collectAsState()
    val isLoading = loginState is ApiResponse.Loading

    val passwordFocus = remember { FocusRequester() }

    LaunchedEffect(loginState) {
        when (val result = loginState) {
            is ApiResponse.Success -> {
                navController.navigate(Routes.DASHBOARD) {
                    popUpTo(0) { inclusive = true }
                }
                viewModel.resetLoginState()
            }

            is ApiResponse.Failure -> {
                error = result.errorMessage
                viewModel.resetLoginState()
            }

            else -> Unit
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocus),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(if (passwordVisible) "🙈" else "👁️")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    error = null
                    if (!areNotEmpty(email, password)) {
                        error = "Completa todos los campos"
                        return@KeyboardActions
                    }
                    if (!isValidEmail(email)) {
                        error = "Email no válido"
                        return@KeyboardActions
                    }
                    if (!isStrongPassword(password)) {
                        error = "Contraseña muy débil"
                        return@KeyboardActions
                    }
                    viewModel.login(email, password)
                },
            ),
        )

        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = error ?: "", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                error = null
                if (!areNotEmpty(email, password)) {
                    error = "Completa todos los campos"
                    return@Button
                }
                if (!isValidEmail(email)) {
                    error = "Email no válido"
                    return@Button
                }
                if (!isStrongPassword(password)) {
                    error = "Contraseña muy débil"
                    return@Button
                }
                viewModel.login(email, password)
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Iniciar Sesión")
            }
        }

        TextButton(
            onClick = { navController.navigate(Routes.FORGOT_PASSWORD) },
            modifier = Modifier.align(Alignment.End),
        ) {
            Text(text = "¿Olvidaste tu contraseña?")
        }
    }
}
