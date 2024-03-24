package hu.tb.minichefy.presentation.screens.recipe.recipe_details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.recipe.SimpleQuickRecipeInfo
import hu.tb.minichefy.presentation.preview.RecipePreviewParameterProvider
import hu.tb.minichefy.presentation.screens.components.icons.iconVectorResource
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.components.ConfirmRecipeAddToStorageDialog
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.components.OneColorBackground
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.components.QuickInfoBox
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SMALL_SPACE_BETWEEN_ELEMENTS
import kotlinx.coroutines.launch

@Composable
fun RecipeDetailsScreen(
    viewModel: RecipeDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RecipeDetailsContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun RecipeDetailsContent(
    uiState: RecipeDetailsViewModel.UiState,
    onEvent: (RecipeDetailsViewModel.OnEvent) -> Unit,
) {
    var isConfirmDialogVisible by remember {
        mutableStateOf(false)
    }

    uiState.recipe?.let { recipe ->
        Column {
            DetailsTopContent(
                modifier = Modifier
                    .weight(1f),
                recipe = recipe
            )
            Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
            DetailsBottomContent(
                modifier = Modifier
                    .weight(1f),
                recipe = recipe
            )
        }
    }

    if (isConfirmDialogVisible && uiState.isInformDialogShouldShow) {
        ConfirmRecipeAddToStorageDialog(
            onConfirmButtonClick = {
                onEvent(RecipeDetailsViewModel.OnEvent.ShouldDialogAppear(it))
                onEvent(RecipeDetailsViewModel.OnEvent.MakeRecipe)
                isConfirmDialogVisible = false
            },
            onCancelButtonClick = {
                onEvent(RecipeDetailsViewModel.OnEvent.ShouldDialogAppear(it))
                isConfirmDialogVisible = false
            }
        )
    }
}

@Composable
private fun DetailsTopContent(
    modifier: Modifier = Modifier,
    recipe: Recipe
) {
    Column(
        modifier = modifier
            .padding(horizontal = SCREEN_HORIZONTAL_PADDING)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = iconVectorResource(iconResource = recipe.icon),
                contentDescription = "recipe image"
            )
        }
        Spacer(modifier = Modifier.height(SMALL_SPACE_BETWEEN_ELEMENTS))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = recipe.title,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
        QuickInfoBox(
            infoList = listOf(
                SimpleQuickRecipeInfo(recipe.quantity.toString(), "serve"),
                SimpleQuickRecipeInfo(recipe.timeToCreate.toString(), recipe.timeUnit.toString())
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailsBottomContent(
    modifier: Modifier = Modifier,
    recipe: Recipe,
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = 22.dp,
                    topEnd = 22.dp
                )
            )
    ) {
        OneColorBackground()
        Column {
            TabRow(
                modifier = Modifier,
                selectedTabIndex = pagerState.settledPage
            ) {
                Tab(selected = pagerState.settledPage == 0, onClick = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(0)
                    }
                },
                    text = {
                        Text(
                            text = "Ingredients",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    })
                Tab(selected = pagerState.settledPage == 1, onClick = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(1)
                    }
                },
                    text = {
                        Text(
                            text = "Steps",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    })
            }
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth(),
                state = pagerState
            ) { pageIndex ->
                when (pageIndex) {
                    0 -> {
                        LazyColumn {
                            items(
                                items = recipe.ingredientList
                            ) {
                                ListItem(headlineContent = { Text(text = it.title) })
                            }
                        }
                    }

                    1 -> {
                        LazyColumn {
                            items(
                                items = recipe.howToSteps
                            ) {
                                ListItem(headlineContent = { Text(text = it.step) })
                            }
                        }
                    }
                }
            }

        }

    }
}

@Preview
@Composable
fun RecipeDetailsContentPreview(
    @PreviewParameter(RecipePreviewParameterProvider::class) mockRecipe: Recipe
) {
    RecipeDetailsContent(
        uiState = RecipeDetailsViewModel.UiState(
            recipe = mockRecipe
        ),
        onEvent = {}
    )
}