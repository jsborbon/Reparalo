package com.jsborbon.reparalo.screens.authentication.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.UserType
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.ui.theme.RepairYellow
import com.jsborbon.reparalo.utils.ValidationUtils.areNotEmpty
import com.jsborbon.reparalo.utils.ValidationUtils.isStrongPassword
import com.jsborbon.reparalo.utils.ValidationUtils.isValidEmail
import com.jsborbon.reparalo.utils.ValidationUtils.isValidPhone
import com.jsborbon.reparalo.utils.ValidationUtils.passwordsMatch
import com.jsborbon.reparalo.viewmodels.AuthViewModel

@Composable
fun SignUpForm(navController: NavController, viewModel: AuthViewModel) {
    var fullNameInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var phoneNumberInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var confirmPasswordInput by remember { mutableStateOf("") }
    var selectedUserType by remember { mutableStateOf(UserType.CLIENT) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val registrationState by viewModel.signUpState.collectAsState()
    val isRegistering = registrationState is ApiResponse.Loading

    val emailFocusRequester = remember { FocusRequester() }
    val phoneFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val formScale by animateFloatAsState(
        targetValue = if (isRegistering) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "formScale"
    )

    LaunchedEffect(registrationState) {
        when (val currentState = registrationState) {
            is ApiResponse.Success -> {
                focusManager.clearFocus()
                navController.navigate(Routes.DASHBOARD) {
                    popUpTo(0) { inclusive = true }
                }
                viewModel.resetSignUpState()
            }
            is ApiResponse.Failure -> {
                errorMessage = currentState.errorMessage
                viewModel.resetSignUpState()
            }
            else -> Unit
        }
    }

    val performRegistration = {
        errorMessage = null
        focusManager.clearFocus()

        when {
            !areNotEmpty(fullNameInput, emailInput, phoneNumberInput, passwordInput, confirmPasswordInput) ->
                errorMessage = "Completa todos los campos"
            !isValidEmail(emailInput) ->
                errorMessage = "Email no válido"
            !isValidPhone(phoneNumberInput) ->
                errorMessage = "Teléfono inválido"
            !isStrongPassword(passwordInput) ->
                errorMessage = "Contraseña muy débil"
            !passwordsMatch(passwordInput, confirmPasswordInput) ->
                errorMessage = "Las contraseñas no coinciden"
            else -> {
                viewModel.signUp(emailInput, passwordInput, fullNameInput, phoneNumberInput, selectedUserType.name)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .scale(formScale)
            .semantics {
                contentDescription = "Formulario de registro de nueva cuenta"
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Enhanced user type selection card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Selección de tipo de usuario"
                },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "👤 Tipo de cuenta:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Client option
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .selectable(
                                selected = selectedUserType == UserType.CLIENT,
                                onClick = { selectedUserType = UserType.CLIENT },
                                role = Role.RadioButton
                            ),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedUserType == UserType.CLIENT)
                                RepairYellow.copy(alpha = 0.2f)
                            else
                                MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (selectedUserType == UserType.CLIENT) 4.dp else 2.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RadioButton(
                                selected = selectedUserType == UserType.CLIENT,
                                onClick = { selectedUserType = UserType.CLIENT },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = RepairYellow
                                ),
                                modifier = Modifier.semantics {
                                    contentDescription = "Seleccionar tipo de usuario cliente"
                                }
                            )
                            Column {
                                Text(
                                    "🙋‍♂️ Cliente",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (selectedUserType == UserType.CLIENT)
                                        FontWeight.Bold
                                    else
                                        FontWeight.Normal
                                )
                                Text(
                                    "Busco reparaciones",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Technician option
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .selectable(
                                selected = selectedUserType == UserType.TECHNICIAN,
                                onClick = { selectedUserType = UserType.TECHNICIAN },
                                role = Role.RadioButton
                            ),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedUserType == UserType.TECHNICIAN)
                                RepairYellow.copy(alpha = 0.2f)
                            else
                                MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (selectedUserType == UserType.TECHNICIAN) 4.dp else 2.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RadioButton(
                                selected = selectedUserType == UserType.TECHNICIAN,
                                onClick = { selectedUserType = UserType.TECHNICIAN },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = RepairYellow
                                ),
                                modifier = Modifier.semantics {
                                    contentDescription = "Seleccionar tipo de usuario técnico"
                                }
                            )
                            Column {
                                Text(
                                    "🔧 Técnico",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (selectedUserType == UserType.TECHNICIAN)
                                        FontWeight.Bold
                                    else
                                        FontWeight.Normal
                                )
                                Text(
                                    "Ofrezco servicios",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Enhanced form fields card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "📝 Registro manual",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Full name input field
                OutlinedTextField(
                    value = fullNameInput,
                    onValueChange = {
                        fullNameInput = it
                        errorMessage = null
                    },
                    label = {
                        Text(
                            "Nombre completo",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = "Icono de persona",
                            tint = RepairYellow
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = "Campo para ingresar nombre completo"
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { emailFocusRequester.requestFocus() }
                    ),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RepairYellow,
                        focusedLabelColor = RepairYellow,
                        cursorColor = RepairYellow,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
                    ),
                    singleLine = true,
                    enabled = !isRegistering
                )

                // Email input field
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = {
                        emailInput = it.trim()
                        errorMessage = null
                    },
                    label = {
                        Text(
                            "Email",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Email,
                            contentDescription = "Icono de email",
                            tint = RepairYellow
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(emailFocusRequester)
                        .semantics {
                            contentDescription = "Campo para ingresar email"
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { phoneFocusRequester.requestFocus() }
                    ),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RepairYellow,
                        focusedLabelColor = RepairYellow,
                        cursorColor = RepairYellow,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
                    ),
                    singleLine = true,
                    enabled = !isRegistering
                )

                // Phone number input field
                OutlinedTextField(
                    value = phoneNumberInput,
                    onValueChange = {
                        phoneNumberInput = it
                        errorMessage = null
                    },
                    label = {
                        Text(
                            "Teléfono",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Phone,
                            contentDescription = "Icono de teléfono",
                            tint = RepairYellow
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(phoneFocusRequester)
                        .semantics {
                            contentDescription = "Campo para ingresar número de teléfono"
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { passwordFocusRequester.requestFocus() }
                    ),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RepairYellow,
                        focusedLabelColor = RepairYellow,
                        cursorColor = RepairYellow,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
                    ),
                    singleLine = true,
                    enabled = !isRegistering
                )

                // Password input field
                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = {
                        passwordInput = it
                        errorMessage = null
                    },
                    label = {
                        Text(
                            "Contraseña",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Lock,
                            contentDescription = "Icono de contraseña",
                            tint = RepairYellow
                        )
                    },
                    visualTransformation = if (isPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = { isPasswordVisible = !isPasswordVisible },
                            modifier = Modifier.semantics {
                                contentDescription = if (isPasswordVisible)
                                    "Ocultar contraseña"
                                else
                                    "Mostrar contraseña"
                            }
                        ) {
                            Icon(
                                painter = if (isPasswordVisible)
                                    painterResource(id=R.drawable.outline_visibility_off)
                                else
                                    painterResource(id=R.drawable.baseline_visibility),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(passwordFocusRequester)
                        .semantics {
                            contentDescription = "Campo para ingresar contraseña"
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { confirmPasswordFocusRequester.requestFocus() }
                    ),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RepairYellow,
                        focusedLabelColor = RepairYellow,
                        cursorColor = RepairYellow,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
                    ),
                    singleLine = true,
                    enabled = !isRegistering
                )

                // Confirm password input field
                OutlinedTextField(
                    value = confirmPasswordInput,
                    onValueChange = {
                        confirmPasswordInput = it
                        errorMessage = null
                    },
                    label = {
                        Text(
                            "Confirmar Contraseña",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Lock,
                            contentDescription = "Icono de confirmar contraseña",
                            tint = RepairYellow
                        )
                    },
                    visualTransformation = if (isConfirmPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
                            modifier = Modifier.semantics {
                                contentDescription = if (isConfirmPasswordVisible)
                                    "Ocultar confirmación de contraseña"
                                else
                                    "Mostrar confirmación de contraseña"
                            }
                        ) {
                            Icon(
                                painter = if (isConfirmPasswordVisible)
                                    painterResource(id=R.drawable.outline_visibility_off)
                                else
                                    painterResource(id=R.drawable.baseline_visibility),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(confirmPasswordFocusRequester)
                        .semantics {
                            contentDescription = "Campo para confirmar contraseña"
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { performRegistration() }
                    ),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RepairYellow,
                        focusedLabelColor = RepairYellow,
                        cursorColor = RepairYellow,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
                    ),
                    singleLine = true,
                    enabled = !isRegistering
                )
            }
        }

        // Enhanced animated error message display
        AnimatedVisibility(
            visible = errorMessage != null,
            enter = fadeIn(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + expandVertically() + scaleIn(initialScale = 0.95f),
            exit = fadeOut() + shrinkVertically() + scaleOut(targetScale = 0.95f)
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Text(
                                    text = "⚠️",
                                    fontSize = 16.sp
                                )
                            }
                        }
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.semantics {
                                contentDescription = "Error: ${errorMessage ?: ""}"
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Enhanced registration button with loading state
        Button(
            onClick = performRegistration,
            enabled = !isRegistering &&
                fullNameInput.isNotBlank() &&
                emailInput.isNotBlank() &&
                phoneNumberInput.isNotBlank() &&
                passwordInput.isNotBlank() &&
                confirmPasswordInput.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Minimum touch target size
                .semantics {
                    contentDescription = if (isRegistering)
                        "Registrando cuenta, por favor espere"
                    else
                        "Botón para crear cuenta nueva"
                },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = RepairYellow,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = RepairYellow.copy(alpha = 0.6f),
                disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 4.dp,
                disabledElevation = 2.dp
            )
        ) {
            if (isRegistering) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Text(
                        "Creando cuenta...",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Text(
                    "🚀 Crear Cuenta",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enhanced link to login
        TextButton(
            onClick = {
                if (!isRegistering) {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            },
            enabled = !isRegistering,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .semantics {
                    contentDescription = "Ir a iniciar sesión"
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "¿Ya tienes cuenta?",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "🔑 Iniciar Sesión",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
