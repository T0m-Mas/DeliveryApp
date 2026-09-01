package com.mrgndt.delivery.ui.screen.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mrgndt.delivery.ui.theme.DeliveryAppTheme

@Composable
fun LocationConfirmDeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.errorContainer,
        title = {
            Text(
                text = "Eliminar Lugar",
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        },
        text = {
            Text(
                text = "¿Esta seguro que desea eliminar este lugar?",
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Preview
@Composable
fun DeleteDialogPreview() {
    DeliveryAppTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            LocationConfirmDeleteDialog(
                onDismissRequest = {},
                onConfirmation = {}
            )

        }
    }
}

