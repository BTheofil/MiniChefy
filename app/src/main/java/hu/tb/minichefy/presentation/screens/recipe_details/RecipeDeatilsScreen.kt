package hu.tb.minichefy.presentation.screens.recipe_details

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun RecipeDetailsScreen(
    navigateToRecipeList: () -> Unit,
    recipeId: Int? = 0
) {
    Text(text = "details screen" + recipeId.toString())
}