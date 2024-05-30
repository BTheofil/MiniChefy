package hu.tb.minichefy.presentation.screens.recipe.recipe_list.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import hu.tb.minichefy.presentation.screens.recipe.recipe_list.RecipeListScreen

const val RECIPE_LIST_ROUTE = "recipe_list_route"

fun NavController.navigateToRecipeList() {
    this.navigate(RECIPE_LIST_ROUTE) {
        popUpTo(0)
    }
}

fun NavGraphBuilder.recipeListNavigation(
    navigateToCreateRecipe: () -> Unit,
    navigateToRecipeDetails: (Long) -> Unit,
) {
    composable(
        route = RECIPE_LIST_ROUTE,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        RecipeListScreen(
            onFloatingButtonClick = navigateToCreateRecipe,
            onItemClick = navigateToRecipeDetails
        )
    }
}