package hu.tb.minichefy.presentation.screens.storage.storage_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.presentation.screens.components.PlusFAB
import hu.tb.minichefy.presentation.screens.storage.storage_list.componenets.StorageItem

@Composable
fun StorageListScreen(
    viewModel: StorageListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StorageScreenContent(
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageScreenContent(
    uiState: StorageListViewModel.UiState
) {
    Scaffold(
        floatingActionButton = {
            PlusFAB {}
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 22.dp, vertical = 8.dp)
        ) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth(),
                query = uiState.searchText,
                onQueryChange = {},
                onSearch = {},
                placeholder = {
                    Text(text = "Search food")
                },
                active = false,
                onActiveChange = {}
            ) {}
            Spacer(modifier = Modifier.height(22.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = uiState.filterList
                ) { filter ->
                    FilterChip(
                        selected = false,
                        onClick = { /*TODO*/ },
                        label = { Text(text = filter) })
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth(),
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = uiState.foodList
                ) {
                    StorageItem()
                }
            }
        }
    }
}

@Preview
@Composable
fun MainScreenContentPreview() {
    StorageScreenContent(
        StorageListViewModel.UiState(
            filterList = listOf("fruit", "vegetable"),
            foodList = listOf("apple", "pear", "banana", "melon", "onion")
        )
    )
}
