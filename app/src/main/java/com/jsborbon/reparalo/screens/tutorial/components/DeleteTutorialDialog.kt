package com.jsborbon.reparalo.screens.tutorial.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteTutorialDialog(
    tutorialId: String?,
    onConfirm: (String?) -> Unit,
    onDismiss: () -> Unit,
) {
    if (tutorialId != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Eliminar tutorial") },
            text = { Text("¿Estás seguro de que deseas eliminar este tutorial? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = { onConfirm(tutorialId) }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            },
        )
    }
}
