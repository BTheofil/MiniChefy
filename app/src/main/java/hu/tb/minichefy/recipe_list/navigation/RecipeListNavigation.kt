package hu.tb.minichefy.recipe_list.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import hu.tb.minichefy.recipe_list.RecipeListScreen

const val RECIPE_LIST_ROUTE = "recipe_list_route"

fun NavController.navigateToRecipeList() {
    this.navigate(RECIPE_LIST_ROUTE)
}

fun NavGraphBuilder.recipeListNavigation(){
    composable(route = RECIPE_LIST_ROUTE){
        RecipeListScreen()
    }
}