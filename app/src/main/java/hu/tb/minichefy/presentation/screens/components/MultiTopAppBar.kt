package hu.tb.minichefy.presentation.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.tb.minichefy.presentation.ui.LocalModalDrawerState
import hu.tb.minichefy.presentation.ui.theme.MiniChefyTheme
import kotlinx.coroutines.launch

sealed class AppBarType {
    data class SearchAppBar(
        val queryText: String,
        val onQueryChange: (text: String) -> Unit,
        val clearButtonClick: () -> Unit
    ) : AppBarType()

    data class BasicAppBar(
        val title: String
    ) : AppBarType()

    data class BackNavigationWithActionsAppBar(
        val actions: @Composable RowScope.() -> Unit,
        val onBackButtonClick: () -> Unit
    ) : AppBarType()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiTopAppBar(
    appBarType: AppBarType,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
) {
    val scope = rememberCoroutineScope()
    val drawerState = LocalModalDrawerState.current

    TopAppBar(
        title = {
            when (appBarType) {
                is AppBarType.SearchAppBar -> SearchItemBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    queryText = appBarType.queryText,
                    onQueryChange = appBarType.onQueryChange,
                    clearIconButtonClick = appBarType.clearButtonClick
                )

                is AppBarType.BasicAppBar -> Text(
                    text = appBarType.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                else -> Unit
            }
        },
        colors = colors,
        actions = {
            when (appBarType) {
                is AppBarType.BackNavigationWithActionsAppBar -> appBarType.actions.invoke(this)
                else -> Unit
            }
        },
        navigationIcon = {
            when (appBarType) {
                is AppBarType.BackNavigationWithActionsAppBar -> {
                    IconButton(onClick = appBarType.onBackButtonClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "back arrow icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                is AppBarType.BasicAppBar, is AppBarType.SearchAppBar -> IconButton(onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Menu, contentDescription = "menu icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun MultiTopAppBarPreview() {
    MiniChefyTheme {
        Column {
            MultiTopAppBar(
                appBarType = AppBarType.SearchAppBar(
                    queryText = "apple",
                    onQueryChange = {},
                    clearButtonClick = {}
                )
            )
            MultiTopAppBar(
                appBarType = AppBarType.BasicAppBar(
                    title = "title",
                )
            )
            MultiTopAppBar(
                appBarType = AppBarType.BackNavigationWithActionsAppBar(
                    actions = {
                        Icon(imageVector = Icons.Rounded.Menu, contentDescription = "")
                    },
                    onBackButtonClick = {}
                )
            )
        }
    }
}