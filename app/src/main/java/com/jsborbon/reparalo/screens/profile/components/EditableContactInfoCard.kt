package com.jsborbon.reparalo.screens.profile.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.utils.ValidationUtils.validateEmail
import com.jsborbon.reparalo.utils.ValidationUtils.validateName
import com.jsborbon.reparalo.utils.ValidationUtils.validatePhone
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class ContactFormData(
    val name: String,
    val email: String,
    val phone: String,
    val joinDate: String,
    val location: String? = null
)

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

data class FieldValidation(
    val name: ValidationResult = ValidationResult(true),
    val email: ValidationResult = ValidationResult(true),
    val phone: ValidationResult = ValidationResult(true)
)

@Composable
fun EditableContactInfoCard(
    contactData: ContactFormData,
    onContactDataChange: (ContactFormData) -> Unit,
    modifier: Modifier = Modifier,
    isEditing: Boolean = false,
    onEditToggle: ((Boolean) -> Unit)? = null,
    onSave: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    isSaving: Boolean = false,
    showValidation: Boolean = true,
    isReadOnly: Boolean = false,
) {
    var internalEditing by remember { mutableStateOf(isEditing) }
    var hasChanges by remember { mutableStateOf(false) }
    var originalData by remember { mutableStateOf(contactData) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val nameFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    val phoneFocusRequester = remember { FocusRequester() }

    // Validation
    val validation by remember {
        derivedStateOf {
            FieldValidation(
                name = validateName(contactData.name),
                email = validateEmail(contactData.email),
                phone = validatePhone(contactData.phone)
            )
        }
    }

    val isFormValid = validation.name.isValid &&
        validation.email.isValid &&
        validation.phone.isValid

    // Calculate membership duration
    val membershipDuration = remember(contactData.joinDate) {
        try {
            val joinLocalDate = LocalDate.parse(contactData.joinDate, DateTimeFormatter.ISO_LOCAL_DATE)
            val daysSince = ChronoUnit.DAYS.between(joinLocalDate, LocalDate.now())
            when {
                daysSince < 30 -> "$daysSince días"
                daysSince < 365 -> "${daysSince / 30} meses"
                else -> "${daysSince / 365} años"
            }
        } catch (_: Exception) {
            "Tiempo no disponible"
        }
    }

    // Update hasChanges when data changes
    LaunchedEffect(contactData) {
        hasChanges = contactData != originalData
    }

    // Handle edit mode changes
    LaunchedEffect(isEditing) {
        internalEditing = isEditing
        if (isEditing) {
            originalData = contactData
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(dampingRatio = 0.7f)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (internalEditing) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (internalEditing) 6.dp else 4.dp
        ),
        border = if (internalEditing) {
            androidx.compose.foundation.BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
        } else null
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header
            ContactFormHeader(
                title = "Información de contacto",
                isEditing = internalEditing,
                hasChanges = hasChanges,
                isReadOnly = isReadOnly,
                onEditToggle = { editing ->
                    internalEditing = editing
                    onEditToggle?.invoke(editing)
                    if (editing) {
                        originalData = contactData
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Progress indicator when saving
            AnimatedVisibility(
                visible = isSaving,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Form fields
            if (internalEditing && !isReadOnly) {
                EditMode(
                    contactData = contactData,
                    onContactDataChange = onContactDataChange,
                    validation = validation,
                    showValidation = showValidation,
                    nameFocusRequester = nameFocusRequester,
                    emailFocusRequester = emailFocusRequester,
                    phoneFocusRequester = phoneFocusRequester,
                    membershipDuration = membershipDuration
                )
            } else {
                ViewMode(
                    contactData = contactData,
                    membershipDuration = membershipDuration
                )
            }

            // Action buttons
            if (internalEditing && !isReadOnly) {
                Spacer(modifier = Modifier.height(20.dp))
                EditModeActions(
                    hasChanges = hasChanges,
                    isFormValid = isFormValid,
                    isSaving = isSaving,
                    onSave = {
                        onSave?.invoke()
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    },
                    onCancel = {
                        onContactDataChange(originalData)
                        internalEditing = false
                        onEditToggle?.invoke(false)
                        onCancel?.invoke()
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
            }
        }
    }
}

@Composable
private fun ContactFormHeader(
    title: String,
    isEditing: Boolean,
    hasChanges: Boolean,
    isReadOnly: Boolean,
    onEditToggle: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isEditing) Icons.Default.Edit else Icons.Default.Person,
                contentDescription = null,
                tint = if (isEditing) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (hasChanges && isEditing) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            MaterialTheme.colorScheme.secondary,
                            CircleShape
                        )
                )
            }
        }

        if (!isReadOnly) {
            AnimatedContent(
                targetState = isEditing,
                transitionSpec = {
                    slideInVertically { -it } + fadeIn() togetherWith
                        slideOutVertically { it } + fadeOut()
                },
                label = "edit_button"
            ) { editing ->
                if (!editing) {
                    OutlinedButton(
                        onClick = { onEditToggle(true) },
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar")
                    }
                }
            }
        }
    }
}

@Composable
private fun EditMode(
    contactData: ContactFormData,
    onContactDataChange: (ContactFormData) -> Unit,
    validation: FieldValidation,
    showValidation: Boolean,
    nameFocusRequester: FocusRequester,
    emailFocusRequester: FocusRequester,
    phoneFocusRequester: FocusRequester,
    membershipDuration: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Name field
        ValidatedTextField(
            value = contactData.name,
            onValueChange = { newValue ->
                onContactDataChange(contactData.copy(name = newValue))
            },
            label = "Nombre completo",
            icon = Icons.Default.Person,
            validation = validation.name,
            showValidation = showValidation,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { emailFocusRequester.requestFocus() }
            ),
            modifier = Modifier.focusRequester(nameFocusRequester)
        )

        // Email field
        ValidatedTextField(
            value = contactData.email,
            onValueChange = { newValue ->
                onContactDataChange(contactData.copy(email = newValue))
            },
            label = "Correo electrónico",
            icon = Icons.Default.Email,
            validation = validation.email,
            showValidation = showValidation,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { phoneFocusRequester.requestFocus() }
            ),
            modifier = Modifier.focusRequester(emailFocusRequester)
        )

        // Phone field
        ValidatedTextField(
            value = contactData.phone,
            onValueChange = { newValue ->
                onContactDataChange(contactData.copy(phone = newValue))
            },
            label = "Número de teléfono",
            icon = Icons.Default.Phone,
            validation = validation.phone,
            showValidation = showValidation,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.focusRequester(phoneFocusRequester)
        )

        // Location field (if available)
        contactData.location?.let { location ->
            OutlinedTextField(
                value = location,
                onValueChange = { newValue ->
                    onContactDataChange(contactData.copy(location = newValue))
                },
                label = { Text("Ubicación") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person, // Replace with location icon
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Read-only join date
        MembershipInfoCard(
            joinDate = contactData.joinDate,
            membershipDuration = membershipDuration
        )
    }
}

@Composable
private fun ViewMode(
    contactData: ContactFormData,
    membershipDuration: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ContactInfoDisplay(
            icon = Icons.Default.Person,
            label = "Nombre",
            value = contactData.name
        )

        ContactInfoDisplay(
            icon = Icons.Default.Email,
            label = "Email",
            value = contactData.email
        )

        ContactInfoDisplay(
            icon = Icons.Default.Phone,
            label = "Teléfono",
            value = contactData.phone
        )

        contactData.location?.let { location ->
            ContactInfoDisplay(
                icon = Icons.Default.Person, // Replace with location icon
                label = "Ubicación",
                value = location
            )
        }

        MembershipInfoCard(
            joinDate = contactData.joinDate,
            membershipDuration = membershipDuration
        )
    }
}

@Composable
private fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    validation: ValidationResult,
    showValidation: Boolean,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val isError = showValidation && !validation.isValid

    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = when {
                        isError -> MaterialTheme.colorScheme.error
                        value.isNotEmpty() && validation.isValid -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            },
            trailingIcon = {
                if (showValidation && value.isNotEmpty()) {
                    Icon(
                        imageVector = if (validation.isValid) Icons.Default.Check else Icons.Default.Clear,
                        contentDescription = null,
                        tint = if (validation.isValid) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            isError = isError,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
        )

        AnimatedVisibility(
            visible = isError && validation.errorMessage != null,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Text(
                text = validation.errorMessage ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
private fun ContactInfoDisplay(
    icon: ImageVector,
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun MembershipInfoCard(
    joinDate: String,
    membershipDuration: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id=R.drawable.baseline_date_range),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Miembro desde",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = joinDate,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = membershipDuration,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun EditModeActions(
    hasChanges: Boolean,
    isFormValid: Boolean,
    isSaving: Boolean,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.weight(1f),
            enabled = !isSaving
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cancelar")
        }

        Button(
            onClick = onSave,
            modifier = Modifier.weight(1f),
            enabled = hasChanges && isFormValid && !isSaving,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            if (isSaving) {
                androidx.compose.material3.CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (isSaving) "Guardando..." else "Guardar")
        }
    }
}

@Composable
fun EditableContactInfoCard(
    name: String,
    phone: String,
    joinDate: String,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var contactData by remember {
        mutableStateOf(
            ContactFormData(
                name = name,
                email = "",
                phone = phone,
                joinDate = joinDate
            )
        )
    }

    EditableContactInfoCard(
        contactData = contactData,
        onContactDataChange = { newData ->
            contactData = newData
            onNameChange(newData.name)
            onPhoneChange(newData.phone)
        },
        modifier = modifier,
        isEditing = true
    )
}
