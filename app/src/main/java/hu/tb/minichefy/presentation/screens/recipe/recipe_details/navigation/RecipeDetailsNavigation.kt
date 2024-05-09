package hu.tb.minichefy.presentation.screens.recipe.recipe_details.navigation

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.RecipeDetailsScreen

const val RECIPE_DETAILS_ROUTE = "recipe_details_route"
const val RECIPE_ID_ARGUMENT_KEY = "recipeId"

fun NavController.navigateToRecipeDetails(recipeId: Long) {
    this.navigate(RECIPE_DETAILS_ROUTE + "/${recipeId}")
}

fun NavGraphBuilder.recipeDetailsNavigation() {
    composable(route = "$RECIPE_DETAILS_ROUTE/{$RECIPE_ID_ARGUMENT_KEY}",
        enterTransition = {
            fadeIn(
                animationSpec = tween(durationMillis = 700, easing = LinearEasing)
            )
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        RecipeDetailsScreen()
    }
}