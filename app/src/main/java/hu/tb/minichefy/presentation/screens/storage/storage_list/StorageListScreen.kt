package hu.tb.minichefy.presentation.screens.storage.storage_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.presentation.screens.components.IconSelectorSheet
import hu.tb.minichefy.presentation.screens.components.PlusFAB
import hu.tb.minichefy.presentation.screens.components.SearchItemBar
import hu.tb.minichefy.presentation.screens.components.icons.IconManager
import hu.tb.minichefy.presentation.screens.storage.components.ProductTagSelectorDialog
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
    var isEditProductTagSelectorOpen by remember {
        mutableStateOf(false)
    }

    var isIconSelectorOpen by remember {
        mutableStateOf(false)
    }

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
                clearIconButtonClick = { onEvent(StorageListViewModel.OnEvent.ClearSearchText) }
            )
            Spacer(modifier = Modifier.height(22.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = uiState.foodTagList
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
                itemsIndexed(
                    items = uiState.foodList,
                    key = { _, item -> item.id!! },
                    contentType = { _, item -> item }
                ) { index, food ->
                    AnimatedContent(
                        targetState = uiState.modifiedProductIndex == index,
                        transitionSpec = {
                            scaleIn().togetherWith(scaleOut())
                        }, label = "edit product animation"
                    ) { isFoodEdited ->
                        when (isFoodEdited) {
                            true -> EditStorageItem(
                                food = food,
                                onIconClick = { isIconSelectorOpen = true },
                                onCloseClick = { onEvent(StorageListViewModel.OnEvent.SaveEditedFood) },
                                onDeleteTagClick = { tag ->
                                    onEvent(
                                        StorageListViewModel.OnEvent.ModifyProductTag(tag)
                                    )
                                },
                                onAddTagClick = {
                                    isEditProductTagSelectorOpen = true
                                },
                                onChangeQuantity = { value ->
                                    onEvent(
                                        StorageListViewModel.OnEvent.ModifyProductQuantity(value)
                                    )
                                },
                            )

                            false -> StorageItem(
                                food = food,
                                onEditClick = {
                                    onEvent(StorageListViewModel.OnEvent.UpdateModifyProductIndex(index))
                                })
                        }
                    }
                }
            }
        }
    }

    if (isIconSelectorOpen) {
        IconSelectorSheet(
            allIconList = uiState.allProductIconList,
            selectedIcon = IconManager().convertIntToProductIcon(uiState.foodList[uiState.modifiedProductIndex].icon)!!,
            onItemClick = { onEvent(StorageListViewModel.OnEvent.ModifyProductIcon(it)) },
            onDismissRequest = { isIconSelectorOpen = false }
        )
    }

    if (isEditProductTagSelectorOpen) {
        ProductTagSelectorDialog(
            dismissAndCloseAction = {
                isEditProductTagSelectorOpen = false
            },
            onTagClick = {
                onEvent(
                    StorageListViewModel.OnEvent.ModifyProductTag(it)
                )
            },
            allTagList = uiState.foodTagList,
            selectedTagList = uiState.foodList[uiState.modifiedProductIndex].foodTagList.orEmpty()
        )
    }
}

@Preview
@Composable
fun MainScreenContentPreview() {

    val testIcon = IconManager().getRandomProduct()

    StorageScreenContent(
        StorageListViewModel.UiState(
            foodTagList = listOf(FoodTag(0, "fruit"), FoodTag(1, "vegetable")),
            foodList = listOf(
                Food(
                    id = 1,
                    icon = testIcon.resource,
                    title = "apple",
                    quantity = 2f,
                    unitOfMeasurement = UnitOfMeasurement.KG,
                    foodTagList = null
                ),
                Food(
                    id = 2,
                    icon = testIcon.resource,
                    title = "banana",
                    quantity = 4f,
                    unitOfMeasurement = UnitOfMeasurement.DKG,
                    foodTagList = listOf(FoodTag(0, "fruit"), FoodTag(2, "fruit"))
                )
            )
        ),
        onEvent = {},
        onFABClick = {}
    )
}
