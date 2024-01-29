package hu.tb.minichefy.presentation.screens.recipe.recipe_create.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.CreateRecipe

const val CREATE_RECIPE_ROUTE = "create_recipe_route"

fun NavController.navigateToCreateRecipe() {
    this.navigate(CREATE_RECIPE_ROUTE)
}

fun NavGraphBuilder.createRecipeNavigation(
    onFinishButtonClick: () -> Unit
){
    composable(route = CREATE_RECIPE_ROUTE){
        CreateRecipe(
            onFinishRecipeButtonClick = onFinishButtonClick,
        )
    }
}