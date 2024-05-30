package hu.tb.minichefy.presentation.screens.storage.storage_list.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import hu.tb.minichefy.presentation.screens.storage.storage_list.StorageListScreen

const val MAIN_STORAGE_ROUTE = "main_storage_route"

fun NavController.navigateToStorageList() {
    this.navigate(MAIN_STORAGE_ROUTE)
}

fun NavGraphBuilder.storageListNavigation(navigateToCreateScreen: () -> Unit) {
    composable(
        route = MAIN_STORAGE_ROUTE,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        StorageListScreen(onFABClick = navigateToCreateScreen)
    }
}