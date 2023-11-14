package hu.tb.minichefy.recipe_details.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import hu.tb.minichefy.recipe_details.RecipeDetailsScreen

const val RECIPE_DETAILS_ROUTE = "recipe_details_route"

fun NavController.navigateToRecipeDetails() {
    this.navigate(RECIPE_DETAILS_ROUTE)
}

fun NavGraphBuilder.recipeDetailsNavigation(navigateToRecipeList: () -> Unit) {
    composable(route = RECIPE_DETAILS_ROUTE) {
        RecipeDetailsScreen(
            navigateToRecipeList
        )
    }
}