package com.jsborbon.reparalo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.utils.ValidationUtils.isNotEmpty
import com.jsborbon.reparalo.utils.ValidationUtils.isStrongPassword
import com.jsborbon.reparalo.utils.ValidationUtils.isValidEmail
import com.jsborbon.reparalo.utils.ValidationUtils.isValidPhone
import com.jsborbon.reparalo.utils.ValidationUtils.passwordsMatch
import kotlinx.coroutines.launch

@Composable
fun AuthenticationScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val isLoginState = remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Logo y título
            Text(
                text = "Reparalo",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = "Tu asistente de reparaciones",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Pestañas para cambiar entre login y registro
            TabRow(
                selectedTabIndex = if (isLoginState.value) 0 else 1,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Tab(
                    selected = isLoginState.value,
                    onClick = { isLoginState.value = true },
                    text = { Text("Iniciar Sesión") },
                )
                Tab(
                    selected = !isLoginState.value,
                    onClick = { isLoginState.value = false },
                    text = { Text("Registrarse") },
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (isLoginState.value) {
                LoginContent(viewModel, navController)
            } else {
                SignUpContent(viewModel, navController)
            }
        }
    }
}

@Composable
fun LoginContent(
    viewModel: AuthViewModel,
    navController: NavController,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            singleLine = true,
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(if (passwordVisible) "🙈" else "👁️")
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )

        // Error
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Start),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Login button
        Button(
            onClick = {
                // Validaciones
                if (!isNotEmpty(email, password)) {
                    errorMessage = "Por favor complete todos los campos"
                    return@Button
                }
                if (!isValidEmail(email)) {
                    errorMessage = "El correo electrónico no es válido"
                    return@Button
                }
                if (!isStrongPassword(password)) {
                    errorMessage = "La contraseña es muy débil"
                    return@Button
                }

                isLoading = true
                errorMessage = null

                coroutineScope.launch {
                    viewModel.login(
                        email = email,
                        password = password,
                        onSuccess = {
                            navController.navigate(Routes.DASHBOARD) {
                                popUpTo(Routes.AUTHENTICATION) { inclusive = true }
                            }
                        },
                        onError = { err ->
                            errorMessage = err
                        },
                    )
                    isLoading = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
                Text("Iniciar Sesión")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { /* recuperar contraseña */ }) {
            Text("¿Olvidaste tu contraseña?")
        }
    }
}

@Composable
fun SignUpContent(
    viewModel: AuthViewModel,
    navController: NavController,
) {
    // Estados de formulario
    val nombreState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val telefonoState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val confirmPasswordState = remember { mutableStateOf("") }
    val passwordVisibleState = remember { mutableStateOf(false) }
    val tipoUsuarioState = remember { mutableStateOf("cliente") }
    val isLoadingState = remember { mutableStateOf(false) }
    val errorMessageState = remember { mutableStateOf<String?>(null) }

    val nombre by nombreState
    val email by emailState
    val telefono by telefonoState
    val password by passwordState
    val confirmPassword by confirmPasswordState
    val passwordVisible by passwordVisibleState
    val tipoUsuario by tipoUsuarioState
    val isLoading by isLoadingState
    val errorMessage by errorMessageState

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombreState.value = it },
            label = { Text("Nombre") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(16.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { emailState.value = it },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(16.dp))

        // Teléfono
        OutlinedTextField(
            value = telefono,
            onValueChange = { telefonoState.value = it },
            label = { Text("Teléfono") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(16.dp))

        // Tipo de usuario
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Tipo de usuario:", modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(Modifier.width(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = tipoUsuario == "cliente",
                    onClick = { tipoUsuarioState.value = "cliente" },
                )
                Text("Cliente", modifier = Modifier.padding(start = 4.dp))
            }
            Spacer(Modifier.width(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = tipoUsuario == "tecnico",
                    onClick = { tipoUsuarioState.value = "tecnico" },
                )
                Text("Técnico", modifier = Modifier.padding(start = 4.dp))
            }
        }

        Spacer(Modifier.height(16.dp))

        // Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { passwordState.value = it },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisibleState.value = !passwordVisible }) {
                    Text(if (passwordVisible) "🙈" else "👁️")
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(16.dp))

        // Confirmar contraseña
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPasswordState.value = it },
            label = { Text("Confirmar Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
        )

        // Error
        if (errorMessage != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Start),
            )
        }

        Spacer(Modifier.height(24.dp))

        // Botón de registro
        Button(
            onClick = {
                // Validaciones
                if (!isNotEmpty(nombre, email, telefono, password, confirmPassword)) {
                    errorMessageState.value = "Por favor complete todos los campos"
                    return@Button
                }
                if (!isValidEmail(email)) {
                    errorMessageState.value = "El correo electrónico no es válido"
                    return@Button
                }
                if (!isStrongPassword(password)) {
                    errorMessageState.value = "La contraseña es muy débil"
                    return@Button
                }
                if (!passwordsMatch(password, confirmPassword)) {
                    errorMessageState.value = "Las contraseñas no coinciden"
                    return@Button
                }
                if (!isValidPhone(telefono)) {
                    errorMessageState.value = "Número de teléfono inválido"
                    return@Button
                }

                isLoadingState.value = true
                errorMessageState.value = null

                coroutineScope.launch {
                    viewModel.signUp(
                        email = email,
                        password = password,
                        nombre = nombre,
                        telefono = telefono,
                        tipoUsuario = tipoUsuario,
                        onSuccess = {
                            navController.navigate(Routes.DASHBOARD) {
                                popUpTo(Routes.AUTHENTICATION) { inclusive = true }
                            }
                        },
                        onError = { err ->
                            errorMessageState.value = err
                        },
                    )
                    isLoadingState.value = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
                Text("Registrarse")
            }
        }
    }
}
