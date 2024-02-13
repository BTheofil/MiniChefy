package hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.presentation.screens.components.QuestionRowAnswer

@Composable
fun QuestionForm(
    recipeTitleValue: String,
    recipeTitleOnValueChange: (String) -> Unit,
    onAddQuantityClick: () -> Unit,
    onRemoveQuantityClick: () -> Unit,
    quantityContent: Int,
    isErrorHappened: Boolean
) {
    Column {
        QuestionRowAnswer(
            questionText = "Recipe title:",
            textFieldValue = recipeTitleValue,
            onTextFieldValueChange = recipeTitleOnValueChange
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "How many times can eat?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        AddRemoveRow(
            onAddButtonClick = { onAddQuantityClick() },
            onRemoveButtonClick = { onRemoveQuantityClick() },
            displayContent = quantityContent,
            isErrorHappened = isErrorHappened
        )
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
        isErrorHappened = false
    )
}