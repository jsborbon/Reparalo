package com.jsborbon.reparalo.screens.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    navController: NavController,
    viewModel: AuthViewModel = remember { AuthViewModel() }
){
    val context = LocalContext.current

    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cambiar Contraseña") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    errorMessage = null
                    successMessage = null
                },
                label = { Text("Nueva contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    errorMessage = null
                    successMessage = null
                },
                label = { Text("Confirmar contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
            )

            errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            successMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.primary)
            }

            Button(
                onClick = {
                    when {
                        newPassword != confirmPassword -> {
                            errorMessage = "Las contraseñas no coinciden"
                        }

                        newPassword.length < 6 -> {
                            errorMessage = "La contraseña debe tener al menos 6 caracteres"
                        }

                        else -> {
                            isSubmitting = true
                            viewModel.changePassword(newPassword) { result ->
                                isSubmitting = false
                                when (result) {
                                    is ApiResponse.Success -> {
                                        successMessage = "Contraseña actualizada correctamente"
                                        Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    }
                                    is ApiResponse.Failure -> {
                                        errorMessage = result.errorMessage
                                    }
                                    else -> {}
                                }
                            }
                        }
                    }
                },
                enabled = !isSubmitting,
                modifier = Modifier.align(Alignment.End),
            ) {
                Text("Guardar")
            }
        }
    }
}
