package hu.tb.minichefy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.tb.minichefy.new_recipe.CreateRecipe

@Composable
fun MainNavigation() {
    NavHost(navController = rememberNavController(), startDestination = "create"){
        composable(route = "create"){
            CreateRecipe()
        }
    }
}