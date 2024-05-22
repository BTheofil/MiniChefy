package hu.tb.minichefy.presentation.screens.recipe.recipe_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.presentation.preview.RecipePreviewParameterProvider
import hu.tb.minichefy.presentation.screens.components.PlusFAB
import hu.tb.minichefy.presentation.screens.components.MultiTopAppBar
import hu.tb.minichefy.presentation.screens.components.SettingsPanel
import hu.tb.minichefy.presentation.screens.components.TopAppBarType
import hu.tb.minichefy.presentation.screens.components.extensions.clickableWithoutRipple
import hu.tb.minichefy.presentation.screens.recipe.recipe_list.components.RecipeItem
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SCREEN_VERTICAL_PADDING

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeListScreenContent(
    uiState: RecipeListViewModel.UiState,
    onEvent: (RecipeListViewModel.OnEvent) -> Unit,
    onFloatingButtonClick: () -> Unit,
    onRecipeItemClick: (Long) -> Unit
) {
    var settingPanelVisible by remember {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            MultiTopAppBar(
                appBarType = TopAppBarType.SearchAppBar(
                    queryText = uiState.searchRecipeText,
                    onQueryChange = { onEvent(RecipeListViewModel.OnEvent.SearchTextChange(it)) },
                    clearButtonClick = { onEvent(RecipeListViewModel.OnEvent.SearchTextChange("")) }
                ),
            )
        },
        floatingActionButton = {
            PlusFAB { onFloatingButtonClick() }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(
                        horizontal = SCREEN_HORIZONTAL_PADDING,
                        vertical = SCREEN_VERTICAL_PADDING * 4
                    )
            ) {
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
                    dismissSettingPanel = {
                        settingPanelVisible = false
                    },
                    onDeleteItemClick = {
                        onEvent(RecipeListViewModel.OnEvent.DeleteRecipe)
                        settingPanelVisible = false
                    }
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