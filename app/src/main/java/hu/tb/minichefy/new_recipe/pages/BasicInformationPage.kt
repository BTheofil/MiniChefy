package hu.tb.minichefy.new_recipe.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.tb.minichefy.new_recipe.components.AddRemoveRow

@Composable
fun BasicInformationPage(
    recipeName: String,
    onGiveTitleValueChange: (String) -> Unit,
    onRemoveQuantityClick: () -> Unit,
    onAddQuantityClick: () -> Unit,
    onNextPageClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = recipeName,
            onValueChange = onGiveTitleValueChange,
            label = {
                Text(text = "Give name the recipe")
            },
        )
        Text(text = "How many times can eat?")
        AddRemoveRow(
            onAddButtonClick = onAddQuantityClick,
            onRemoveButtonClick = onRemoveQuantityClick
        )
        OutlinedButton(onClick = onNextPageClick) {
            Text(text = "Next page")
        }
    }
}

@Preview
@Composable
fun BasicInformationPagePreview() {
    BasicInformationPage("test name", {}, {}, {}, {})
}