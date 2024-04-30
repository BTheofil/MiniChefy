package hu.tb.minichefy.presentation.screens.settings.settings_options.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
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
                text = "Create new tag",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = onValueChange
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
        onValueChange = {}
    )
}