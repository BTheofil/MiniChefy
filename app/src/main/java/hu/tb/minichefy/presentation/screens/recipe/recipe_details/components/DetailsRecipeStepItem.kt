package hu.tb.minichefy.presentation.screens.recipe.recipe_details.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DetailsRecipeStepItem(
    stepNumber: Int,
    stepTextDescription: String,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        CircleNumberComponents(stepNumber.toString())
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = stepTextDescription,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview
@Composable
fun RecipeStepItemPreview() {
    DetailsRecipeStepItem(
        1,
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nibh orci, varius nec molestie vitae, venenatis quis leo. Suspendisse euismod nisl eget mi maximus, eget gravida felis pulvinar. In mollis ac est sed tristique. Praesent tempus faucibus dui ut hendrerit. Phasellus tempor eget mauris at dignissim."
    )
}