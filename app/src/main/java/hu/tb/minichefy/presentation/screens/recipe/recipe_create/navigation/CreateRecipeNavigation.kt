package hu.tb.minichefy.presentation.screens.recipe.recipe_create.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.CreateRecipeScreen

const val CREATE_RECIPE_ROUTE = "create_recipe_route"
const val EDIT_RECIPE_ARGUMENT_KEY = "editRecipeId"

fun NavController.navigateToCreateRecipe(editRecipeId: Long? = null) {
    this.navigate("$CREATE_RECIPE_ROUTE?$EDIT_RECIPE_ARGUMENT_KEY=$editRecipeId")
}

fun NavGraphBuilder.createRecipeNavigation(
    onFinishButtonClick: () -> Unit
) {
    composable(
        route = "$CREATE_RECIPE_ROUTE?$EDIT_RECIPE_ARGUMENT_KEY={$EDIT_RECIPE_ARGUMENT_KEY}",
        arguments = listOf(navArgument(EDIT_RECIPE_ARGUMENT_KEY) { nullable = true })
    ) {
        CreateRecipeScreen(
            onFinishRecipeButtonClick = onFinishButtonClick,
        )
    }
}