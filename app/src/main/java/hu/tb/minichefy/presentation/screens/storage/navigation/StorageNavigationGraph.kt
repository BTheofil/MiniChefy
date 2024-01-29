package hu.tb.minichefy.presentation.screens.storage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import hu.tb.minichefy.presentation.screens.storage.main.MainScreen

const val STORAGE_GRAPH = "storage_graph"

fun NavGraphBuilder.storageNestedGraph(navController: NavController){
    navigation(startDestination = "storageMainScreen", route = STORAGE_GRAPH) {
        composable(
            route = "storageMainScreen"
        ) {
            MainScreen()
        }
    }
}