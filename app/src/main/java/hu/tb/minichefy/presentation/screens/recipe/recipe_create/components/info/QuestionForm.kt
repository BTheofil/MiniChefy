package hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.presentation.ui.components.bottomBorder

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
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recipe title: ",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .bottomBorder(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 1.dp
                    ),
                value = recipeTitleValue,
                onValueChange = recipeTitleOnValueChange,
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        }
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