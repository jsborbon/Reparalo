package com.jsborbon.reparalo.screens.authentication.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
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
import com.jsborbon.reparalo.models.UserType
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.utils.ValidationUtils
import com.jsborbon.reparalo.viewmodels.AuthViewModel

@Composable
fun SignUpForm(navController: NavController, viewModel: AuthViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf(UserType.CLIENT) }
    var passwordVisible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val signUpState by viewModel.signUpState.collectAsState()
    val isLoading = signUpState is ApiResponse.Loading

    val nameFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val phoneFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val confirmFocus = remember { FocusRequester() }

    LaunchedEffect(signUpState) {
        when (signUpState) {
            is ApiResponse.Success -> {
                navController.navigate(Routes.DASHBOARD) {
                    popUpTo(0) { inclusive = true }
                }
                viewModel.resetSignUpState()
            }

            is ApiResponse.Failure -> {
                error = (signUpState as ApiResponse.Failure).errorMessage
                viewModel.resetSignUpState()
            }

            ApiResponse.Loading, ApiResponse.Idle -> Unit
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(nameFocus),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { emailFocus.requestFocus() }),
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(emailFocus),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { phoneFocus.requestFocus() }),
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Teléfono") },
            leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(phoneFocus),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Tipo de usuario:")
            Spacer(modifier = Modifier.width(12.dp))
            RadioButton(selected = userType == UserType.CLIENT, onClick = { userType = UserType.CLIENT })
            Text("Cliente")
            Spacer(modifier = Modifier.width(12.dp))
            RadioButton(selected = userType == UserType.TECHNICIAN, onClick = { userType = UserType.TECHNICIAN })
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
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocus),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { confirmFocus.requestFocus() }),
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Contraseña") },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(confirmFocus),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                error = null
                if (!ValidationUtils.areNotEmpty(name, email, phone, password, confirmPassword)) {
                    error = "Completa todos los campos"
                    return@KeyboardActions
                }
                if (!ValidationUtils.isValidEmail(email)) {
                    error = "Email no válido"
                    return@KeyboardActions
                }
                if (!ValidationUtils.isStrongPassword(password)) {
                    error = "Contraseña débil"
                    return@KeyboardActions
                }
                if (!ValidationUtils.passwordsMatch(password, confirmPassword)) {
                    error = "Las contraseñas no coinciden"
                    return@KeyboardActions
                }
                if (!ValidationUtils.isValidPhone(phone)) {
                    error = "Teléfono inválido"
                    return@KeyboardActions
                }
                viewModel.signUp(email, password, name, phone, userType.name)
            }),
        )

        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = error.orEmpty(), color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                error = null
                if (!ValidationUtils.areNotEmpty(name, email, phone, password, confirmPassword)) {
                    error = "Completa todos los campos"
                    return@Button
                }
                if (!ValidationUtils.isValidEmail(email)) {
                    error = "Email no válido"
                    return@Button
                }
                if (!ValidationUtils.isStrongPassword(password)) {
                    error = "Contraseña débil"
                    return@Button
                }
                if (!ValidationUtils.passwordsMatch(password, confirmPassword)) {
                    error = "Las contraseñas no coinciden"
                    return@Button
                }
                if (!ValidationUtils.isValidPhone(phone)) {
                    error = "Teléfono inválido"
                    return@Button
                }
                viewModel.signUp(email, password, name, phone, userType.name)
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Registrarse")
            }
        }
    }
}
