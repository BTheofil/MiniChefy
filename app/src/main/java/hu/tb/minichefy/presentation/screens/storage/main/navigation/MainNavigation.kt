package hu.tb.minichefy.presentation.screens.storage.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import hu.tb.minichefy.presentation.screens.storage.main.MainScreen

const val MAIN_STORAGE_ROUTE = "main_storage_route"

fun NavController.navigateToMainStorage() {
    this.navigate(MAIN_STORAGE_ROUTE)
}

fun NavGraphBuilder.mainStorageNavigation(){
    composable(route = MAIN_STORAGE_ROUTE){
        MainScreen()
    }
}