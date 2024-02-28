package hu.tb.minichefy.presentation.screens.storage.storage_create.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import hu.tb.minichefy.presentation.screens.storage.storage_create.StorageCreateScreen

const val STORAGE_CREATE_ROUTE = "storage_create_route"

fun NavController.navigateToStorageCreate() {
    this.navigate(STORAGE_CREATE_ROUTE)
}

fun NavGraphBuilder.storageCreateNavigation(navigateToListScreen: () -> Unit) {
    composable(route = STORAGE_CREATE_ROUTE){
        StorageCreateScreen(
            saveSuccess = navigateToListScreen
        )
    }
}