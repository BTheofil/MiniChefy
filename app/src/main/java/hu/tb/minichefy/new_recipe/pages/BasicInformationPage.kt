package hu.tb.minichefy.new_recipe.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BasicInformationPage() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = {
                Text(text = "Give name the recipe")
            },
        )
        Text(text = "How many times can eat?")

    }
}

@Preview
@Composable
fun BasicInformationPagePreview() {
    BasicInformationPage()
}