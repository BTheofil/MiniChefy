package hu.tb.minichefy.new_recipe.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import hu.tb.minichefy.new_recipe.CreateRecipe

const val CREATE_RECIPE_ROUTE = "create_recipe_route"

fun NavGraphBuilder.createRecipeNavigation(
    onNextButtonClick: () -> Unit
){
    composable(route = CREATE_RECIPE_ROUTE){
        CreateRecipe(
            onNextButtonClick = onNextButtonClick,
        )
    }
}