package hu.tb.minichefy.presentation.screens.recipe_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import hu.tb.minichefy.domain.model.Recipe

@Composable
fun RecipeDetailsScreen(
    viewModel: RecipeDetailsViewModel = hiltViewModel(),
    navigateToRecipeList: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RecipeDetailsContent(uiState)
}

@Composable
fun RecipeDetailsContent(
    uiState: RecipeDetailsViewModel.UiState
) {
    Column {
        Image(imageVector = Icons.Default.AccountCircle, contentDescription = "recipe image")
        Text(text = uiState.recipe!!.name)
    }
}

@Preview
@Composable
fun RecipeDetailsContentPreview() {
    RecipeDetailsContent(
        uiState = RecipeDetailsViewModel.UiState(
            recipe = Recipe(
                id = 0,
                name = "test",
                quantity = 1,
                howToSteps = emptyList()
            )
        )
    )
}