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
import androidx.compose.ui.Alignment
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
import hu.tb.minichefy.presentation.screens.components.MultiTopAppBar
import hu.tb.minichefy.presentation.screens.components.SettingsPanel
import hu.tb.minichefy.presentation.screens.components.TopAppBarType
import hu.tb.minichefy.presentation.screens.components.extensions.clickableWithoutRipple
import hu.tb.minichefy.presentation.screens.storage.components.ProductTagSelectorDialog
import hu.tb.minichefy.presentation.screens.storage.storage_list.componenets.EditQuantityDialog
import hu.tb.minichefy.presentation.screens.storage.storage_list.componenets.EditStorageItem
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SCREEN_VERTICAL_PADDING
import hu.tb.minichefy.presentation.util.icons.IconManager
import hu.tb.minichefy.presentation.util.icons.iconVectorResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun StorageListScreen(
    viewModel: StorageListViewModel = hiltViewModel(),
    onFABClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StorageScreenContent(
        uiState = uiState,
        uiEvent = viewModel.uiEvent,
        onFABClick = onFABClick,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StorageScreenContent(
    uiState: StorageListViewModel.UiState,
    uiEvent: Flow<StorageListViewModel.UiEvent>,
    onFABClick: () -> Unit,
    onAction: (StorageListViewModel.OnAction) -> Unit
) {
    var isEditProductTagSelectorOpen by remember {
        mutableStateOf(false)
    }
    var isEditQuantityDialogOpen by remember {
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

    LaunchedEffect(Unit) {
        uiEvent.collect {
            isEditQuantityDialogOpen = when (it) {
                StorageListViewModel.UiEvent.CloseModifyQuantityDialog -> false
                StorageListViewModel.UiEvent.OpenModifyQuantityDialog -> true
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .clickableWithoutRipple {
                focusManager.clearFocus()
            },
        topBar = {
            MultiTopAppBar(
                appBarType = TopAppBarType.SearchAppBar(
                    queryText = uiState.searchText,
                    onQueryChange = { onAction(StorageListViewModel.OnAction.SearchTextChange(it)) },
                    clearButtonClick = {
                        onAction(
                            StorageListViewModel.OnAction.SearchTextChange(
                                ""
                            )
                        )
                    })
            )
        },
        floatingActionButton = {
            PlusFAB(onClick = onFABClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(
                    horizontal = SCREEN_HORIZONTAL_PADDING,
                    vertical = SCREEN_VERTICAL_PADDING * 4
                )
        ) {
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
                        onClick = { onAction(StorageListViewModel.OnAction.FilterChipClicked(filter)) },
                        label = {
                            Text(
                                text = filter.tag,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        })
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
                        targetState = uiState.modifyFoodListPositionIndex == index,
                        transitionSpec = {
                            scaleIn().togetherWith(scaleOut())
                        }, label = "edit product animation"
                    ) { isFoodEdited ->
                        when (isFoodEdited) {
                            true -> EditStorageItem(
                                food = food,
                                onFoodIconClick = { isIconSelectorOpen = true },
                                onCloseClick = { onAction(StorageListViewModel.OnAction.ClearSelectedFoodIndex) },
                                onDeleteTagClick = { tag ->
                                    onAction(
                                        StorageListViewModel.OnAction.ModifyFoodTags(tag)
                                    )
                                },
                                onAddTagClick = {
                                    isEditProductTagSelectorOpen = true
                                },
                                onQuantityClick = {
                                    onAction(StorageListViewModel.OnAction.SetupEditFoodQuantityDialog)
                                }
                            )

                            false -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    ListItem(
                                        modifier = Modifier
                                            .combinedClickable(
                                                onClick = {
                                                    onAction(
                                                        StorageListViewModel.OnAction.FoodEditButtonClick(
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

    if (isEditQuantityDialogOpen) {
        EditQuantityDialog(
            quantityValue = uiState.quantityModifyValue,
            onQuantityChange = { quantityString ->
                onAction(StorageListViewModel.OnAction.ModifyFoodDialogQuantityChange(quantityString))
            },
            isQuantityHasError = uiState.isQuantityModifyDialogHasError,
            measurementValue = uiState.unitOfMeasurementModifyValue,
            onMeasurementChange = { uof ->
                onAction(StorageListViewModel.OnAction.EditFoodDialogUnitChange(uof))
            },
            onCancelButtonClick = { isEditQuantityDialogOpen = false },
            onConfirmButtonClick = {
                onAction(StorageListViewModel.OnAction.SaveEditFoodQuantities)
            },
            onDismissRequest = { isEditQuantityDialogOpen = false }
        )
    }

    if (isIconSelectorOpen) {
        IconSelectorSheet(
            allIconList = uiState.allFoodIconList,
            selectedIcon = IconManager().findFoodIconByInt(uiState.foodList[uiState.modifyFoodListPositionIndex].icon),
            onItemClick = { onAction(StorageListViewModel.OnAction.ModifyFoodIcon(it)) },
            onDismissRequest = { isIconSelectorOpen = false }
        )
    }

    if (isSettingPanelOpen) {
        SettingsPanel(
            dismissSettingPanel = { isSettingPanelOpen = false },
            onDeleteItemClick = {
                onAction(StorageListViewModel.OnAction.DeleteFood(settingPanelFoodId!!))
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
                onAction(
                    StorageListViewModel.OnAction.ModifyFoodTags(it)
                )
            },
            allTagList = uiState.foodTagList,
            selectedTagList = uiState.foodList[uiState.modifyFoodListPositionIndex].foodTagList
                ?: emptyList()
        )
    }
}

@Preview
@Composable
fun StorageScreenContentPreview(
    @PreviewParameter(FoodPreviewParameterProvider::class) mockProductList: List<Food>
) {
    StorageScreenContent(
        uiState = StorageListViewModel.UiState(
            foodTagList = listOf(FoodTag(0, "fruit"), FoodTag(1, "vegetable")),
            foodList = mockProductList,
        ),
        uiEvent = flow { },
        onAction = {},
        onFABClick = {}
    )
}
