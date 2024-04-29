package hu.tb.minichefy.presentation.screens.settings.settings_main.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import hu.tb.minichefy.presentation.screens.settings.settings_main.SettingsScreen

const val SETTINGS_MAIN_ROUTE = "settings_main_route"

fun NavGraphBuilder.settingsMainNavigation(navigateToSpecificSettings: (route: String) -> Unit) {
    composable(
        route = SETTINGS_MAIN_ROUTE,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        SettingsScreen(
            onSettingsClick = navigateToSpecificSettings
        )
    }
}