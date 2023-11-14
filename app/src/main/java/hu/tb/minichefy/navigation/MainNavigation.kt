package hu.tb.minichefy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import hu.tb.minichefy.recipe_create.navigation.CREATE_RECIPE_ROUTE
import hu.tb.minichefy.recipe_create.navigation.createRecipeNavigation
import hu.tb.minichefy.recipe_create.navigation.navigateToCreateRecipe
import hu.tb.minichefy.recipe_details.navigation.navigateToRecipeDetails
import hu.tb.minichefy.recipe_details.navigation.recipeDetailsNavigation
import hu.tb.minichefy.recipe_list.navigation.navigateToRecipeList
import hu.tb.minichefy.recipe_list.navigation.recipeListNavigation

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = CREATE_RECIPE_ROUTE){
        createRecipeNavigation(
            onFinishButtonClick = navController::navigateToRecipeList
        )

        recipeListNavigation(
            navigateToCreateRecipe = navController::navigateToCreateRecipe,
            navigateToRecipeDetails = navController::navigateToRecipeDetails
        )

        recipeDetailsNavigation(
            navigateToRecipeList = navController::navigateToRecipeList
        )
    }
}