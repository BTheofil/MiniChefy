package hu.tb.minichefy.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import hu.tb.minichefy.domain.model.navigation.NavigationScreenItem

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
                label = {
                    Text(
                        text = stringResource(id = menuItem.label),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
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
    NavigationScreenItem.Storage,
    NavigationScreenItem.Recipe,
    NavigationScreenItem.Settings
)