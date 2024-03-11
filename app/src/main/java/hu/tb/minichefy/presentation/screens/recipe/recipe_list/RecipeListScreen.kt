package hu.tb.minichefy.presentation.screens.recipe.recipe_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.presentation.preview.RecipePreviewParameterProvider
import hu.tb.minichefy.presentation.screens.components.PlusFAB
import hu.tb.minichefy.presentation.screens.components.SearchItemBar
import hu.tb.minichefy.presentation.screens.recipe.recipe_list.components.RecipeItem
import hu.tb.minichefy.presentation.screens.recipe.recipe_list.components.SettingsPanel
import hu.tb.minichefy.presentation.ui.components.clickableWithoutRipple
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SCREEN_VERTICAL_PADDING
import kotlinx.coroutines.launch

@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel = hiltViewModel(),
    onFloatingButtonClick: () -> Unit,
    onItemClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RecipeListScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onFloatingButtonClick = onFloatingButtonClick,
        onRecipeItemClick = onItemClick
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreenContent(
    uiState: RecipeListViewModel.UiState,
    onEvent: (RecipeListViewModel.OnEvent) -> Unit,
    onFloatingButtonClick: () -> Unit,
    onRecipeItemClick: (Long) -> Unit
) {
    val haptics =
        LocalHapticFeedback.current //docs related https://developer.android.com/jetpack/compose/touch-input/pointer-input/tap-and-press

    var settingPanelVisible by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState()

    val focusManager = LocalFocusManager.current

    Scaffold(
        floatingActionButton = {
            PlusFAB { onFloatingButtonClick() }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(
                        horizontal = SCREEN_HORIZONTAL_PADDING,
                        vertical = SCREEN_VERTICAL_PADDING
                    )
            ) {
                SearchItemBar(
                    queryText = uiState.searchRecipeText,
                    onQueryChange = {
                        onEvent(RecipeListViewModel.OnEvent.SearchTextChange(it))
                    },
                    clearIconButtonClick = {
                        onEvent(RecipeListViewModel.OnEvent.ClearText)
                    }
                )
                Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickableWithoutRipple {
                            focusManager.clearFocus()
                        },
                    columns = GridCells.Fixed(2),
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
                                    onClick = { onRecipeItemClick(recipe.id!!) },
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
                            recipe = recipe
                        )
                    }
                }
            }

            if (settingPanelVisible) {
                SettingsPanel(
                    modalSheetState = modalSheetState,
                    dismissSettingPanel = {
                        scope.launch {
                            modalSheetState.hide()
                        }.invokeOnCompletion {
                            if (!modalSheetState.isVisible) {
                                settingPanelVisible = false
                            }
                        }
                    },
                    onEvent = onEvent
                )
            }
        })
}

@Preview
@Composable
fun RecipeListScreenPreview(
    @PreviewParameter(RecipePreviewParameterProvider::class) recipe: Recipe
) {
    RecipeListScreenContent(
        uiState = RecipeListViewModel.UiState(
            recipeList = listOf(recipe)
        ),
        onEvent = {},
        onFloatingButtonClick = {},
        onRecipeItemClick = {}
    )
}