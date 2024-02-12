package hu.tb.minichefy.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import hu.tb.minichefy.R
import hu.tb.minichefy.presentation.screens.recipe.navigation.RECIPE_GRAPH
import hu.tb.minichefy.presentation.screens.storage.navigation.STORAGE_GRAPH

//related docs: https://developer.android.com/jetpack/compose/navigation#bottom-nav
@Composable
fun BottomBarNavigation(
    navController: NavController,
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        menuItems.forEach { menuItem ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = menuItem.icon),
                        contentDescription = "navigation bar icon"
                    )
                },
                label = { Text(menuItem.label) },
                selected = currentDestination?.hierarchy?.any { it.route == menuItem.route } == true,
                onClick = {
                    navController.navigate(menuItem.route) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

val menuItems = listOf(
    BottomNavigationItem.Storage,
    BottomNavigationItem.Recipe
)

sealed class BottomNavigationItem(val icon: Int, val label: String, val route: String) {
    data object Storage :
        BottomNavigationItem(R.drawable.fridge_icon, "Storage", STORAGE_GRAPH)

    data object Recipe : BottomNavigationItem(R.drawable.outline_book_24, "Recipe", RECIPE_GRAPH)
}