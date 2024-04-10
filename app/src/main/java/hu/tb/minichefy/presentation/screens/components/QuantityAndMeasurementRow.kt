package hu.tb.minichefy.presentation.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.TextFieldWithDropdownMenu
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS

@Composable
fun QuantityAndMeasurementRow(
    quantityValue: String,
    onQuantityChange: (String) -> Unit,
    isQuantityHasError: Boolean,
    measurementOptionList: List<UnitOfMeasurement> = UnitOfMeasurement.entries,
    measurementValue: UnitOfMeasurement,
    onMeasurementChange: (UnitOfMeasurement) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f),
            value = quantityValue,
            onValueChange = onQuantityChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            label = {
                Text(
                    text = "Amount",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            singleLine = true,
            isError = isQuantityHasError
        )
        Spacer(modifier = Modifier.width(MEDIUM_SPACE_BETWEEN_ELEMENTS))
        TextFieldWithDropdownMenu(
            modifier = Modifier
                .weight(1f),
            textFieldValue = measurementValue.name,
            labelFieldText = "Measurement",
            menuItemList = measurementOptionList,
            onMenuItemClick = onMeasurementChange
        )
    }
}

@Preview
@Composable
private fun AmountAndMeasurementRowPreview() {
    QuantityAndMeasurementRow(
        quantityValue = "5",
        onQuantityChange = {},
        isQuantityHasError = false,
        measurementValue = UnitOfMeasurement.PIECE,
        onMeasurementChange = {}
    )
}