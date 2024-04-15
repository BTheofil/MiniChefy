package hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.tb.minichefy.domain.model.recipe.TimeUnit
import hu.tb.minichefy.presentation.screens.components.QuantityAndMeasurementRow
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS

@Composable
fun QuestionForm(
    recipeTitleValue: String,
    recipeTitleOnValueChange: (String) -> Unit,
    isTitleHasError: Boolean,
    onAddQuantityClick: () -> Unit,
    onRemoveQuantityClick: () -> Unit,
    quantityContent: Int,
    isCountHasError: Boolean,
    timeFieldValue: String,
    timeFieldValueChange: (String) -> Unit,
    timeUnitValue: TimeUnit,
    timeUnitValueChange: (TimeUnit) -> Unit,
    isTimeHasError: Boolean
) {
    Column {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = recipeTitleValue,
            onValueChange = recipeTitleOnValueChange,
            label = {
                Text(
                    text = "Recipe title",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            isError = isTitleHasError,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
        Text(
            text = "How many times can eat?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
        AddRemoveRow(
            onAddButtonClick = { onAddQuantityClick() },
            onRemoveButtonClick = { onRemoveQuantityClick() },
            displayContent = quantityContent,
            isErrorHappened = isCountHasError
        )
        Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
        QuantityAndMeasurementRow(
            quantityValue = timeFieldValue,
            onQuantityChange = timeFieldValueChange,
            quantityLabel = "Time to cook",
            isQuantityHasError = isTimeHasError,
            measurementValue = timeUnitValue,
            onMeasurementChange = timeUnitValueChange,
            measurementOptionList = TimeUnit.entries,
            measurementLabel = "Time measurement"
        )
    }
}

@Preview
@Composable
fun QuestionFormPreview() {
    QuestionForm(
        recipeTitleValue = "test recipe title",
        recipeTitleOnValueChange = {},
        isTitleHasError = false,
        onAddQuantityClick = {},
        onRemoveQuantityClick = {},
        quantityContent = 1,
        isCountHasError = false,
        timeFieldValue = "",
        timeFieldValueChange = {},
        timeUnitValue = TimeUnit.MINUTES,
        timeUnitValueChange = {},
        isTimeHasError = false
    )
}