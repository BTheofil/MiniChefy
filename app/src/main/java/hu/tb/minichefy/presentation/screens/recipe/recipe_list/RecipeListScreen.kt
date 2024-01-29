package hu.tb.minichefy.presentation.screens.recipe.recipe_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.domain.model.Recipe
import hu.tb.minichefy.presentation.screens.recipe.recipe_list.components.RecipeItem
import kotlinx.coroutines.launch

@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel = hiltViewModel(),
    onFloatingButtonClick: () -> Unit,
    onItemClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is RecipeListViewModel.UiEvent.OnRecipeClick -> onItemClick(event.recipeId)
            }
        }
    }

    RecipeListScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onFloatingButtonClick = onFloatingButtonClick,
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreenContent(
    uiState: RecipeListViewModel.UiState,
    onEvent: (RecipeListViewModel.OnEvent) -> Unit,
    onFloatingButtonClick: () -> Unit,
) {
    val haptics =
        LocalHapticFeedback.current //docs related https://developer.android.com/jetpack/compose/touch-input/pointer-input/tap-and-press

    var settingPanelVisible by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState()

    val dismissSettingPanel = {
        scope.launch {
            modalSheetState.hide()
        }.invokeOnCompletion {
            if (!modalSheetState.isVisible) {
                settingPanelVisible = false
            }
        }
    }

    val focusManager = LocalFocusManager.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onFloatingButtonClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "create new recipe"
                )
            }
        },
        content = { paddingValues ->

            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    query = uiState.searchRecipeText,
                    onQueryChange = {
                        onEvent(RecipeListViewModel.OnEvent.SearchTextChange(it))
                    },
                    onSearch = {
                        focusManager.clearFocus()
                    },
                    active = false,
                    onActiveChange = {},
                    leadingIcon = {
                        Icon(imageVector = Icons.Rounded.Search, contentDescription = "search icon")
                    },
                    placeholder = {
                        Text(text = "Search recipe")
                    }
                ) {
                }

                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            focusManager.clearFocus()
                        },
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = uiState.recipeList,
                        key = { item -> item.id!! }
                    ) { recipe ->
                        RecipeItem(
                            modifier = Modifier
                                .combinedClickable(
                                    onClick = {
                                        onEvent(
                                            RecipeListViewModel.OnEvent.OnRecipeClick(
                                                recipe.id!!
                                            )
                                        )
                                    },
                                    onLongClick = {
                                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onEvent(
                                            RecipeListViewModel.OnEvent.OpenRecipeSettingsPanel(
                                                recipe.id!!
                                            )
                                        )
                                        settingPanelVisible = true
                                    }
                                ),
                            title = recipe.title
                        )
                    }
                }
            }


            if (settingPanelVisible) {
                ModalBottomSheet(
                    sheetState = modalSheetState,
                    onDismissRequest = {
                        dismissSettingPanel()
                    }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 22.dp)
                            .padding(bottom = 22.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    onClick = {
                                        onEvent(RecipeListViewModel.OnEvent.DeleteRecipe)
                                        dismissSettingPanel()
                                    }
                                ),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete recipe icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(22.dp))
                            Text(
                                text = "Delete recipe",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }
        })
}

@Preview
@Composable
fun RecipeListScreenPreview() {
    RecipeListScreenContent(
        uiState = RecipeListViewModel.UiState(
            recipeList = listOf(
                Recipe(0, "test", quantity = 1, howToSteps = emptyList())
            )
        ),
        onEvent = {},
        onFloatingButtonClick = {},
    )
}