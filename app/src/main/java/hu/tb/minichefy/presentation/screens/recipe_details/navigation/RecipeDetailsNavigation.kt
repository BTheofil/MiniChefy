package hu.tb.minichefy.presentation.screens.recipe_details.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import hu.tb.minichefy.presentation.screens.recipe_details.RecipeDetailsScreen

const val RECIPE_DETAILS_ROUTE = "recipe_details_route"

fun NavController.navigateToRecipeDetails(recipeId: Long) {
    this.navigate(RECIPE_DETAILS_ROUTE + "/${recipeId}")
}

fun NavGraphBuilder.recipeDetailsNavigation(navigateToRecipeList: () -> Unit) {
    composable(route = "$RECIPE_DETAILS_ROUTE/{recipeId}") {
        RecipeDetailsScreen(
            navigateToRecipeList = navigateToRecipeList
        )
    }
}