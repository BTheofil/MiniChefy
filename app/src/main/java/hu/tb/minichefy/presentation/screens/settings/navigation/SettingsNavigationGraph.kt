package hu.tb.minichefy.presentation.screens.settings.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import hu.tb.minichefy.presentation.screens.settings.SettingsScreen

const val SETTINGS_GRAPH = "settings_graph"

fun NavGraphBuilder.settingsNavigation() {
    composable(
        route = SETTINGS_GRAPH,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        SettingsScreen()
    }
}