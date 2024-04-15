package hu.tb.minichefy.presentation.screens.storage.storage_list.componenets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.presentation.screens.components.QuantityAndMeasurementRow
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS

@Composable
fun EditQuantityDialog(
    quantityValue: String,
    onQuantityChange: (String) -> Unit,
    isQuantityHasError: Boolean,
    measurementValue: UnitOfMeasurement,
    onMeasurementChange: (UnitOfMeasurement) -> Unit,
    onDismissRequest: () -> Unit,
    onCancelButtonClick: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                Box(
                    Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        Icons.Rounded.Edit, "dialog warning icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Box(
                    Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Modify quantity count",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                QuantityAndMeasurementRow(
                    quantityValue = quantityValue,
                    onQuantityChange = onQuantityChange,
                    quantityLabel = "Amount",
                    isQuantityHasError = isQuantityHasError,
                    measurementValue = measurementValue,
                    onMeasurementChange = onMeasurementChange,
                    measurementLabel = "Measurement",
                    measurementOptionList = UnitOfMeasurement.entries
                )
                Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                )
                {
                    TextButton(onClick = onCancelButtonClick) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                    TextButton(onClick = onConfirmButtonClick) {
                        Text(
                            text = "Proceed",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun EditQuantityDialogPreview() {
    EditQuantityDialog(
        quantityValue = "5",
        onQuantityChange = {},
        isQuantityHasError = false,
        measurementValue = UnitOfMeasurement.PIECE,
        onMeasurementChange = {},
        onDismissRequest = {},
        onCancelButtonClick = {},
        onConfirmButtonClick = {}
    )
}