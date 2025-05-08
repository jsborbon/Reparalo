package com.jsborbon.reparalo.screens.authentication

import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.utils.ValidationUtils.areNotEmpty
import com.jsborbon.reparalo.utils.ValidationUtils.isStrongPassword
import com.jsborbon.reparalo.utils.ValidationUtils.isValidEmail
import com.jsborbon.reparalo.utils.ValidationUtils.isValidPhone
import com.jsborbon.reparalo.utils.ValidationUtils.passwordsMatch
import com.jsborbon.reparalo.viewmodels.AuthViewModel

@Composable
fun SignUpForm(navController: NavController, viewModel: AuthViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf("cliente") }
    var passwordVisible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val signUpState by viewModel.signUpState.collectAsState()

    LaunchedEffect(signUpState) {
        when (val result = signUpState) {
            is ApiResponse.Success -> {
                navController.navigate(Routes.DASHBOARD) {
                    popUpTo(Routes.AUTHENTICATION) { inclusive = true }
                }
            }

            is ApiResponse.Failure -> {
                error = result.errorMessage
            }

            else -> Unit
        }
    }

    val isLoading = signUpState is ApiResponse.Loading

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Teléfono") },
            leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Tipo de usuario:")
            Spacer(modifier = Modifier.width(12.dp))
            RadioButton(selected = userType == "cliente", onClick = { userType = "cliente" })
            Text("Cliente")
            Spacer(modifier = Modifier.width(12.dp))
            RadioButton(selected = userType == "tecnico", onClick = { userType = "tecnico" })
            Text("Técnico")
        }

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
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Contraseña") },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = error.orEmpty(), color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                error = null
                if (!areNotEmpty(name, email, phone, password, confirmPassword)) {
                    error = "Completa todos los campos"
                    return@Button
                }
                if (!isValidEmail(email)) {
                    error = "Email no válido"
                    return@Button
                }
                if (!isStrongPassword(password)) {
                    error = "Contraseña débil"
                    return@Button
                }
                if (!passwordsMatch(password, confirmPassword)) {
                    error = "Las contraseñas no coinciden"
                    return@Button
                }
                if (!isValidPhone(phone)) {
                    error = "Teléfono inválido"
                    return@Button
                }
                viewModel.signUp(email, password, name, phone, userType)
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Registrarse")
            }
        }
    }
}
