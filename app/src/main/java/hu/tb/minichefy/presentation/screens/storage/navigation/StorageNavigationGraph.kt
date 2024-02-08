package hu.tb.minichefy.presentation.screens.storage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import hu.tb.minichefy.presentation.screens.storage.main.navigation.MAIN_STORAGE_ROUTE
import hu.tb.minichefy.presentation.screens.storage.main.navigation.mainStorageNavigation

const val STORAGE_GRAPH = "storage_graph"

fun NavGraphBuilder.storageNestedGraph(navController: NavController){
    navigation(startDestination = MAIN_STORAGE_ROUTE, route = STORAGE_GRAPH) {
        mainStorageNavigation()
    }
}