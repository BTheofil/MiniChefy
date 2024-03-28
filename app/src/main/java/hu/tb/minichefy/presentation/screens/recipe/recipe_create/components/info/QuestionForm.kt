package hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import hu.tb.minichefy.domain.model.recipe.TimeUnit
import hu.tb.minichefy.presentation.screens.components.QuestionRowAnswer
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.TextFieldWithDropdownMenu
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS

@Composable
fun QuestionForm(
    recipeTitleValue: String,
    recipeTitleOnValueChange: (String) -> Unit,
    onAddQuantityClick: () -> Unit,
    onRemoveQuantityClick: () -> Unit,
    quantityContent: Int,
    isErrorHappened: Boolean,
    timeFieldValue: String,
    timeFieldValueChange: (String) -> Unit,
    timeUnitValue: TimeUnit,
    timeUnitValueChange: (TimeUnit) -> Unit,
) {
    Column {
        QuestionRowAnswer(
            questionText = "Recipe title:",
            textFieldValue = recipeTitleValue,
            onTextFieldValueChange = recipeTitleOnValueChange
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
            isErrorHappened = isErrorHappened
        )
        Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f),
                value = timeFieldValue,
                onValueChange = timeFieldValueChange,
                singleLine = true,
                label = {
                    Text(
                        text = "Time to cook",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Spacer(modifier = Modifier.width(MEDIUM_SPACE_BETWEEN_ELEMENTS))
            Box(
                modifier = Modifier
                    .weight(1f),
            ) {
                TextFieldWithDropdownMenu(
                    textFieldValue = timeUnitValue.name,
                    labelFieldText = "Time measurement",
                    menuItemList = TimeUnit.entries,
                    onMenuItemClick = { timeUnitValueChange(it) }
                )
            }
        }
    }
}

@Preview
@Composable
fun QuestionFormPreview() {
    QuestionForm(
        recipeTitleValue = "test recipe title",
        recipeTitleOnValueChange = {},
        onAddQuantityClick = {},
        onRemoveQuantityClick = {},
        quantityContent = 1,
        isErrorHappened = false,
        timeFieldValue = "",
        timeFieldValueChange = {},
        timeUnitValue = TimeUnit.MINUTES,
        timeUnitValueChange = {}
    )
}