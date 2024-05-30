package hu.tb.minichefy.presentation.screens.recipe.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.navigation.createRecipeNavigation
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.navigation.navigateToCreateRecipe
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.navigation.navigateToRecipeDetails
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.navigation.recipeDetailsNavigation
import hu.tb.minichefy.presentation.screens.recipe.recipe_list.navigation.RECIPE_LIST_ROUTE
import hu.tb.minichefy.presentation.screens.recipe.recipe_list.navigation.navigateToRecipeList
import hu.tb.minichefy.presentation.screens.recipe.recipe_list.navigation.recipeListNavigation

const val RECIPE_GRAPH = "recipe_graph"

fun NavGraphBuilder.recipeNestedGraph(navController: NavController){
    navigation(startDestination = RECIPE_LIST_ROUTE, route = RECIPE_GRAPH){
        createRecipeNavigation(
            onFinishButtonClick = navController::navigateToRecipeList
        )

        recipeListNavigation(
            navigateToCreateRecipe = navController::navigateToCreateRecipe,
            navigateToRecipeDetails = navController::navigateToRecipeDetails
        )
        recipeDetailsNavigation(
            navigateBack = { navController.popBackStack() },
            navigateToEdit = { navController.navigateToCreateRecipe(it) }
        )
    }
}