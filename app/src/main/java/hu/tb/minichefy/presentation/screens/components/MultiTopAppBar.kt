package hu.tb.minichefy.presentation.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.tb.minichefy.presentation.ui.LocalModalDrawerState
import kotlinx.coroutines.launch

sealed class TopAppBarType {
    data class SearchAppBar(
        val queryText: String,
        val onQueryChange: (text: String) -> Unit,
        val clearButtonClick: () -> Unit
    ) : TopAppBarType()

    data class BasicAppBar(
        val title: String
    ) : TopAppBarType()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiTopAppBar(
    appBarType: TopAppBarType,
) {
    val scope = rememberCoroutineScope()
    val drawerState = LocalModalDrawerState.current

    TopAppBar(
        title = {
            when (appBarType) {
                is TopAppBarType.SearchAppBar -> SearchItemBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    queryText = appBarType.queryText,
                    onQueryChange = appBarType.onQueryChange,
                    clearIconButtonClick = appBarType.clearButtonClick
                )

                is TopAppBarType.BasicAppBar -> Text(
                    text = appBarType.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.open()
                }
            }) {
                Icon(
                    imageVector = Icons.Rounded.Menu, contentDescription = "menu icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        })
}

@Preview
@Composable
private fun MultiTopAppBarPreview() {
    MultiTopAppBar(
        appBarType = TopAppBarType.SearchAppBar(
            queryText = "apple",
            onQueryChange = {},
            clearButtonClick = {}
        )
    )
}