package hu.tb.minichefy.presentation.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hu.tb.minichefy.R
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS
import kotlinx.coroutines.launch

@Composable
fun DrawerNavigation(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    navController: NavController,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    var selectedItemRoute by rememberSaveable {
        mutableStateOf(menuItems[1].route)
    }

    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.menu),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                menuItems.forEach { menuItem ->
                    if (menuItems.last() == menuItem) {
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(horizontal = 20.dp),
                            thickness = 1.dp,
                            color = Color.Black
                        )
                    }
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = menuItem.icon),
                                contentDescription = "drawer icon badge",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(id = menuItem.label),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        selected = selectedItemRoute == menuItem.route,
                        onClick = {
                            scope.launch {
                                navController.navigate(menuItem.route) {
                                    popUpTo(0)
                                    launchSingleTop = true
                                }
                                selectedItemRoute = menuItem.route
                                drawerState.close()
                            }
                        })
                }
            }
        },
        content = content
    )
}
