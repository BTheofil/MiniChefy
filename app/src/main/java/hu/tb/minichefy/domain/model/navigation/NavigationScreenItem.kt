package hu.tb.minichefy.domain.model.navigation

import hu.tb.minichefy.R
import hu.tb.minichefy.presentation.screens.recipe.navigation.RECIPE_GRAPH
import hu.tb.minichefy.presentation.screens.settings.navigation.SETTINGS_GRAPH
import hu.tb.minichefy.presentation.screens.storage.navigation.STORAGE_GRAPH

sealed class NavigationScreenItem(val icon: Int, val label: Int, val route: String) {
    data object Storage :
        NavigationScreenItem(
            R.drawable.fridge_icon,
            R.string.storage, STORAGE_GRAPH
        )

    data object Recipe : NavigationScreenItem(
        R.drawable.outline_book_24,
        R.string.recipe, RECIPE_GRAPH
    )

    data object Settings : NavigationScreenItem(
        R.drawable.settings_icon,
        R.string.settings,
        SETTINGS_GRAPH
    )
}