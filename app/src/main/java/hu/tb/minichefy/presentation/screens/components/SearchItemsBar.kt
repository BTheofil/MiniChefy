package hu.tb.minichefy.presentation.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchItemBar(
    queryText: String,
    onQueryChange: (text: String) -> Unit,
    clearIconButtonClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    var isClearIconVisible by remember {
        mutableStateOf(false)
    }

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                isClearIconVisible = it.hasFocus
            },
        query = queryText,
        onQueryChange = onQueryChange,
        onSearch = {
            focusManager.clearFocus()
        },
        active = false,
        onActiveChange = {},
        leadingIcon = {
            Icon(imageVector = Icons.Rounded.Search, contentDescription = "search icon")
        },
        trailingIcon = {
            if (isClearIconVisible) {
                IconButton(onClick = clearIconButtonClick) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = "clear text icon"
                    )
                }
            }
        },
        placeholder = {
            Text(
                text = "Search recipe",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        content = {}
    )
}

@Preview
@Composable
fun SearchItemBarPreview() {
    var queryText by remember {
        mutableStateOf("")
    }

    SearchItemBar(
        queryText = queryText,
        onQueryChange = { queryText = it },
        clearIconButtonClick = { queryText = "" })
}