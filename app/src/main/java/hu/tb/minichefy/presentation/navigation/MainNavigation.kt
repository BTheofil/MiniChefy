package hu.tb.minichefy.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import hu.tb.minichefy.presentation.screens.recipe_create.navigation.createRecipeNavigation
import hu.tb.minichefy.presentation.screens.recipe_create.navigation.navigateToCreateRecipe
import hu.tb.minichefy.presentation.screens.recipe_details.navigation.navigateToRecipeDetails
import hu.tb.minichefy.presentation.screens.recipe_details.navigation.recipeDetailsNavigation
import hu.tb.minichefy.presentation.screens.recipe_list.navigation.RECIPE_LIST_ROUTE
import hu.tb.minichefy.presentation.screens.recipe_list.navigation.navigateToRecipeList
import hu.tb.minichefy.presentation.screens.recipe_list.navigation.recipeListNavigation

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = RECIPE_LIST_ROUTE){
        createRecipeNavigation(
            onFinishButtonClick = navController::navigateToRecipeList
        )

        recipeListNavigation(
            navigateToCreateRecipe = navController::navigateToCreateRecipe,
            navigateToRecipeDetails = navController::navigateToRecipeDetails
        )

        recipeDetailsNavigation()
    }
}