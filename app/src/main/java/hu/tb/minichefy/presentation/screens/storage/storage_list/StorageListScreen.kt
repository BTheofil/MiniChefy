package hu.tb.minichefy.presentation.screens.storage.storage_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.presentation.preview.FoodPreviewParameterProvider
import hu.tb.minichefy.presentation.screens.components.IconSelectorSheet
import hu.tb.minichefy.presentation.screens.components.PlusFAB
import hu.tb.minichefy.presentation.screens.components.SearchItemBar
import hu.tb.minichefy.presentation.screens.components.SettingsPanel
import hu.tb.minichefy.presentation.screens.manager.icons.IconManager
import hu.tb.minichefy.presentation.screens.manager.icons.iconVectorResource
import hu.tb.minichefy.presentation.screens.storage.components.ProductTagSelectorDialog
import hu.tb.minichefy.presentation.screens.storage.storage_list.componenets.EditQuantityDialog
import hu.tb.minichefy.presentation.screens.storage.storage_list.componenets.EditStorageItem
import hu.tb.minichefy.presentation.ui.components.clickableWithoutRipple
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SCREEN_VERTICAL_PADDING

@Composable
fun StorageListScreen(
    viewModel: StorageListViewModel = hiltViewModel(),
    onFABClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val editFoodState by viewModel.editFoodState.collectAsStateWithLifecycle()

    var isEditQuantityDialogOpen by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            isEditQuantityDialogOpen = when (event) {
                StorageListViewModel.UiEvent.CloseEditQuantityDialog -> false

                StorageListViewModel.UiEvent.OpenEditQuantityDialog -> true
            }
        }
    }

    StorageScreenContent(
        uiState = uiState,
        editFoodState = editFoodState,
        onFABClick = onFABClick,
        onEvent = viewModel::onEvent
    )

    if (isEditQuantityDialogOpen) {
        EditQuantityDialog(
            quantityValue = editFoodState.quantityModifyValue,
            onQuantityChange = {
                viewModel.onEvent(StorageListViewModel.OnEvent.EditFoodDialogQuantityChange(it))
            },
            isQuantityHasError = editFoodState.isQuantityModifyDialogHasError,
            measurementValue = editFoodState.unitOfMeasurementModifyValue,
            onMeasurementChange = {
                viewModel.onEvent(StorageListViewModel.OnEvent.EditFoodDialogUnitChange(it))
            },
            onCancelButtonClick = { isEditQuantityDialogOpen = false },
            onConfirmButtonClick = {
                viewModel.onEvent(StorageListViewModel.OnEvent.SaveEditFoodQuantities)
            },
            onDismissRequest = { isEditQuantityDialogOpen = false }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StorageScreenContent(
    uiState: StorageListViewModel.UiState,
    editFoodState: StorageListViewModel.ModifyFoodState,
    onFABClick: () -> Unit,
    onEvent: (StorageListViewModel.OnEvent) -> Unit
) {
    var isEditProductTagSelectorOpen by remember {
        mutableStateOf(false)
    }

    var isIconSelectorOpen by remember {
        mutableStateOf(false)
    }
    var isSettingPanelOpen by remember {
        mutableStateOf(false)
    }
    var settingPanelFoodId by remember {
        mutableStateOf<Long?>(null)
    }

    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .clickableWithoutRipple {
                focusManager.clearFocus()
            },
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
                        targetState = editFoodState.foodListPositionIndex == index,
                        transitionSpec = {
                            scaleIn().togetherWith(scaleOut())
                        }, label = "edit product animation"
                    ) { isFoodEdited ->
                        when (isFoodEdited) {
                            true -> EditStorageItem(
                                food = food,
                                onIconClick = { isIconSelectorOpen = true },
                                onCloseClick = { onEvent(StorageListViewModel.OnEvent.ClearSelectedFoodIndex) },
                                onDeleteTagClick = { tag ->
                                    onEvent(
                                        StorageListViewModel.OnEvent.ModifyFoodTags(tag)
                                    )
                                },
                                onAddTagClick = {
                                    isEditProductTagSelectorOpen = true
                                },
                                onQuantityClick = {
                                    onEvent(StorageListViewModel.OnEvent.SetupEditFoodQuantityDialog)
                                }
                            )

                            false -> {
                                Column {
                                    ListItem(
                                        modifier = Modifier
                                            .combinedClickable(
                                                onClick = {
                                                    onEvent(
                                                        StorageListViewModel.OnEvent.OnProductClick(
                                                            index
                                                        )
                                                    )
                                                },
                                                onLongClick = {
                                                    settingPanelFoodId = food.id
                                                    isSettingPanelOpen = true
                                                }
                                            ),
                                        leadingContent = {
                                            Image(
                                                modifier = Modifier
                                                    .size(64.dp),
                                                imageVector = iconVectorResource(iconResource = food.icon),
                                                contentDescription = "Store icon"
                                            )
                                        },
                                        headlineContent = {
                                            Text(
                                                text = food.title.toUpperCase(Locale.current),
                                                style = MaterialTheme.typography.titleLarge,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        },
                                        supportingContent = {
                                            Text(
                                                modifier = Modifier,
                                                text = food.quantity.toString() + " " + food.unitOfMeasurement,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = MaterialTheme.colorScheme.secondary,
                                                textAlign = TextAlign.Center
                                            )
                                        },
                                        trailingContent = {
                                            Icon(
                                                modifier = Modifier
                                                    .size(32.dp),
                                                imageVector = Icons.Outlined.KeyboardArrowDown,
                                                contentDescription = "add quantity icon",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    )
                                    HorizontalDivider()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (isIconSelectorOpen) {
        IconSelectorSheet(
            allIconList = uiState.allProductIconList,
            selectedIcon = IconManager().convertIntToProductIcon(uiState.foodList[editFoodState.foodListPositionIndex].icon),
            onItemClick = { onEvent(StorageListViewModel.OnEvent.ModifyFoodIcon(it)) },
            onDismissRequest = { isIconSelectorOpen = false }
        )
    }

    if (isSettingPanelOpen) {
        SettingsPanel(
            dismissSettingPanel = { isSettingPanelOpen = false },
            onDeleteItemClick = {
                onEvent(StorageListViewModel.OnEvent.DeleteFood(settingPanelFoodId!!))
                isSettingPanelOpen = false
                settingPanelFoodId = null
            }
        )
    }

    if (isEditProductTagSelectorOpen) {
        ProductTagSelectorDialog(
            dismissAndCloseAction = {
                isEditProductTagSelectorOpen = false
            },
            onTagClick = {
                onEvent(
                    StorageListViewModel.OnEvent.ModifyFoodTags(it)
                )
            },
            allTagList = uiState.foodTagList,
            selectedTagList = uiState.foodList[editFoodState.foodListPositionIndex].foodTagList.orEmpty()
        )
    }
}

@Preview
@Composable
fun StorageScreenContentPreview(
    @PreviewParameter(FoodPreviewParameterProvider::class) mockProductList: List<Food>
) {
    StorageScreenContent(
        StorageListViewModel.UiState(
            foodTagList = listOf(FoodTag(0, "fruit"), FoodTag(1, "vegetable")),
            foodList = mockProductList,
        ),
        StorageListViewModel.ModifyFoodState(),
        onEvent = {},
        onFABClick = {}
    )
}
