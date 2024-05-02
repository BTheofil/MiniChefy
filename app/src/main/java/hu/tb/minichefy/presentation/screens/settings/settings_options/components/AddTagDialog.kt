package hu.tb.minichefy.presentation.screens.settings.settings_options.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import hu.tb.minichefy.R

@Composable
fun AddTagDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    textFieldValue: String,
    onValueChange: (String) -> Unit,
    isError: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Create, contentDescription = "create icon",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(text = stringResource(id = R.string.done))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelClick) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        title = {
            Text(
                text = stringResource(R.string.create_new_tag),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = onValueChange,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary,
                    errorTextColor = MaterialTheme.colorScheme.error
                ),
                isError = isError
            )
        }
    )
}

@Preview
@Composable
private fun AddTagDialogPreview() {
    AddTagDialog(
        onDismissRequest = {},
        onConfirmClick = {},
        onCancelClick = {},
        textFieldValue = "test",
        onValueChange = {},
        isError = false
    )
}