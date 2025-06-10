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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.ui.theme.RepairYellow
import com.jsborbon.reparalo.utils.ValidationUtils.areNotEmpty
import com.jsborbon.reparalo.utils.ValidationUtils.isStrongPassword
import com.jsborbon.reparalo.utils.ValidationUtils.isValidEmail
import com.jsborbon.reparalo.viewmodels.AuthViewModel

@Composable
fun LoginForm(navController: NavController, viewModel: AuthViewModel) {
    // Enhanced state management with improved variable naming
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Collect authentication state
    val authenticationState by viewModel.loginState.collectAsState()
    val isAuthenticating = authenticationState is ApiResponse.Loading

    // Focus management for better UX flow
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Enhanced animations
    val formScale by animateFloatAsState(
        targetValue = if (isAuthenticating) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "formScale"
    )

    // Handle authentication state changes
    LaunchedEffect(authenticationState) {
        when (val currentState = authenticationState) {
            is ApiResponse.Success -> {
                // Clear focus before navigation
                focusManager.clearFocus()
                navController.navigate(Routes.DASHBOARD) {
                    popUpTo(0) { inclusive = true }
                }
                viewModel.resetLoginState()
            }
            is ApiResponse.Failure -> {
                errorMessage = currentState.errorMessage
                viewModel.resetLoginState()
            }
            else -> Unit
        }
    }

    // Function to handle login validation and submission
    val performLogin = {
        errorMessage = null
        focusManager.clearFocus()

        when {
            !areNotEmpty(emailInput, passwordInput) -> {
                errorMessage = "Completa todos los campos"
            }
            !isValidEmail(emailInput) -> {
                errorMessage = "Email no válido"
            }
            !isStrongPassword(passwordInput) -> {
                errorMessage = "Contraseña muy débil"
            }
            else -> {
                viewModel.login(emailInput, passwordInput)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .scale(formScale)
            .semantics {
                contentDescription = "Formulario de inicio de sesión"
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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
                    text = "📧 Iniciar con email",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Enhanced email input field
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = {
                        emailInput = it.trim() // Remove whitespace automatically
                        errorMessage = null // Clear error on input change
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
                        .semantics {
                            contentDescription = "Campo de email para iniciar sesión"
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
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
                    enabled = !isAuthenticating
                )

                // Enhanced password input field
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
                                    painterResource(id= R.drawable.baseline_visibility)
                                else
                                    painterResource(id= R.drawable.outline_visibility_off),                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(passwordFocusRequester)
                        .semantics {
                            contentDescription = "Campo de contraseña para iniciar sesión"
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { performLogin() }
                    ),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RepairYellow,
                        focusedLabelColor = RepairYellow,
                        cursorColor = RepairYellow,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
                    ),
                    singleLine = true,
                    enabled = !isAuthenticating
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

        // Enhanced login button with loading state
        Button(
            onClick = performLogin,
            enabled = !isAuthenticating &&
                emailInput.isNotBlank() && passwordInput.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Minimum touch target size
                .semantics {
                    contentDescription = if (isAuthenticating)
                        "Iniciando sesión, por favor espere"
                    else
                        "Botón para iniciar sesión con email"
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
            if (isAuthenticating) {
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
                        "Iniciando sesión...",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Text(
                    "🚀 Iniciar Sesión",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enhanced forgot password link
        TextButton(
            onClick = {
                if (!isAuthenticating) {
                    navController.navigate(Routes.FORGOT_PASSWORD)
                }
            },
            enabled = !isAuthenticating,
            modifier = Modifier
                .align(Alignment.End)
                .semantics {
                    contentDescription = "Ir a recuperar contraseña"
                }
        ) {
            Text(
                text = "🔑 ¿Olvidaste tu contraseña?",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.End
            )
        }
    }
}
