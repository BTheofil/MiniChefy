package hu.tb.minichefy.presentation.screens.storage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import hu.tb.minichefy.presentation.screens.storage.storage_create.navigation.storageCreateNavigation
import hu.tb.minichefy.presentation.screens.storage.storage_list.navigation.MAIN_STORAGE_ROUTE
import hu.tb.minichefy.presentation.screens.storage.storage_list.navigation.storageListNavigation

const val STORAGE_GRAPH = "storage_graph"

fun NavGraphBuilder.storageNestedGraph(navController: NavController){
    navigation(startDestination = MAIN_STORAGE_ROUTE, route = STORAGE_GRAPH) {
        storageListNavigation()

        storageCreateNavigation()
    }
}