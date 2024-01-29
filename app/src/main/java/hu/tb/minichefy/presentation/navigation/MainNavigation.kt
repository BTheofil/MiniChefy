package hu.tb.minichefy.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import hu.tb.minichefy.presentation.screens.recipe.navigation.recipeNestedGraph
import hu.tb.minichefy.presentation.screens.storage.navigation.STORAGE_GRAPH
import hu.tb.minichefy.presentation.screens.storage.navigation.storageNestedGraph

@Composable
fun MainNavigation(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = STORAGE_GRAPH) {
        storageNestedGraph(navController)

        recipeNestedGraph(navController)
    }
}