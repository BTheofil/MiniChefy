package hu.tb.minichefy.presentation.screens.settings.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import hu.tb.minichefy.presentation.screens.settings.SettingsViewModel
import hu.tb.minichefy.presentation.screens.settings.settings_main.navigation.SETTINGS_MAIN_ROUTE
import hu.tb.minichefy.presentation.screens.settings.settings_main.navigation.settingsMainNavigation
import hu.tb.minichefy.presentation.screens.settings.settings_options.TagScreen

const val SETTINGS_GRAPH = "settings_graph"
const val TAG_SCREEN = "tag_screen"
const val THEME_SCREEN = "theme_screen"

fun NavGraphBuilder.settingsNavigation(navController: NavController) {
    navigation(startDestination = SETTINGS_MAIN_ROUTE, route = SETTINGS_GRAPH) {

        settingsMainNavigation(
            navigateToSpecificSettings = { route ->
                when (route) {
                    TAG_SCREEN -> navController.navigate(TAG_SCREEN)
                    THEME_SCREEN -> navController.navigate(THEME_SCREEN)
                }
            }
        )

        composable(
            route = TAG_SCREEN,
        ) {
            val vm = hiltViewModel<SettingsViewModel>()
            TagScreen(vm.foodTagList)
        }
        composable(
            route = THEME_SCREEN
        ) {
            //todo
        }
    }
}