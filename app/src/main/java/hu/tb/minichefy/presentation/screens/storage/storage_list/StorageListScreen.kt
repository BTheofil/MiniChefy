package hu.tb.minichefy.presentation.screens.storage.storage_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.presentation.screens.components.PlusFAB
import hu.tb.minichefy.presentation.screens.components.SearchItemBar
import hu.tb.minichefy.presentation.screens.storage.storage_list.componenets.EditStorageItem
import hu.tb.minichefy.presentation.screens.storage.storage_list.componenets.StorageItem
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SCREEN_VERTICAL_PADDING

@Composable
fun StorageListScreen(
    viewModel: StorageListViewModel = hiltViewModel(),
    onFABClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StorageScreenContent(
        uiState = uiState,
        onFABClick = onFABClick,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun StorageScreenContent(
    uiState: StorageListViewModel.UiState,
    onFABClick: () -> Unit,
    onEvent: (StorageListViewModel.OnEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            PlusFAB(onClick = onFABClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = SCREEN_HORIZONTAL_PADDING, vertical = SCREEN_VERTICAL_PADDING)
        ) {
            SearchItemBar(
                queryText = uiState.searchText,
                onQueryChange = { onEvent(StorageListViewModel.OnEvent.SearchTextChange(it)) },
                clearIconButtonClick = { onEvent(StorageListViewModel.OnEvent.ClearSearch) }
            )
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
                        selected = uiState.activeFilter.contains(filter),
                        onClick = { onEvent(StorageListViewModel.OnEvent.FilterChipClicked(filter)) },
                        label = { Text(text = filter.tag) })
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = SCREEN_VERTICAL_PADDING),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = uiState.foodList
                ) { food ->
                    AnimatedContent(
                        targetState = food == uiState.editedFood,
                        label = "",
                    ) {
                        when (it) {
                            true -> EditStorageItem {

                            }

                            false -> StorageItem(
                                food = food,
                                onEditClick = {
                                    onEvent(
                                        StorageListViewModel.OnEvent.EditFoodClicked(
                                            food
                                        )
                                    )
                                })
                        }
                    }
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
            filterList = listOf(FoodTag(0, "fruit"), FoodTag(1, "vegetable")),
            foodList = listOf(
                Food(
                    title = "apple",
                    quantity = 2f,
                    unitOfMeasurement = UnitOfMeasurement.KG,
                    foodTagList = null
                ),
                Food(
                    title = "banana",
                    quantity = 4f,
                    unitOfMeasurement = UnitOfMeasurement.DKG,
                    foodTagList = null
                )
            )
        ),
        onEvent = {},
        onFABClick = {}
    )
}
