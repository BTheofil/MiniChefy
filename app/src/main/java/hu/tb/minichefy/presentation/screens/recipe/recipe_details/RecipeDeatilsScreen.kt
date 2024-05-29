package hu.tb.minichefy.presentation.screens.recipe.recipe_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.R
import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.recipe.SimpleQuickRecipeInfo
import hu.tb.minichefy.presentation.preview.RecipePreviewParameterProvider
import hu.tb.minichefy.presentation.screens.components.ImageWidget
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.components.ConfirmRecipeAddToStorageDialog
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.components.DetailsRecipeStepItem
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.components.OneColorBackground
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.components.QuickInfoBox
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SMALL_SPACE_BETWEEN_ELEMENTS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@Composable
fun RecipeDetailsScreen(
    viewModel: RecipeDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RecipeDetailsContent(
        uiState = uiState,
        uiEvent = viewModel.uiEvent,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsContent(
    uiState: RecipeDetailsViewModel.UiState,
    uiEvent: Flow<RecipeDetailsViewModel.UiEvent>,
    onAction: (RecipeDetailsViewModel.OnAction) -> Unit,
) {
    var isConfirmDialogVisible by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                is RecipeDetailsViewModel.UiEvent.ShowSnackBar -> scope.launch {
                    snackbarHostState.showSnackbar(context.getString(event.messageResource, event.argument))
                }
            }
        }
    }

    uiState.recipe?.let { recipe ->
        Scaffold(
            topBar = {
                TopAppBar(title = {},
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    actions = {
                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = {
                                PlainTooltip(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                ) {
                                    Text(
                                        "Cook this recipe",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            },
                            state = rememberTooltipState(),
                        ) {
                            IconButton(
                                modifier = Modifier,
                                onClick = {
                                    isConfirmDialogVisible = uiState.isInformDialogShouldShow
                                    if (!uiState.isInformDialogShouldShow) {
                                        onAction(RecipeDetailsViewModel.OnAction.MakeRecipe)
                                    }
                                }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.chef_hat),
                                    contentDescription = "Make recipe icon",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    })
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) {
            OneColorBackground(color = MaterialTheme.colorScheme.primaryContainer)
            Column {
                DetailsTopContent(
                    modifier = Modifier
                        .padding(it)
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
    }

    if (isConfirmDialogVisible && uiState.isInformDialogShouldShow) {
        ConfirmRecipeAddToStorageDialog(
            onConfirmButtonClick = {
                onAction(RecipeDetailsViewModel.OnAction.ShouldDialogAppear(it))
                onAction(RecipeDetailsViewModel.OnAction.MakeRecipe)
                isConfirmDialogVisible = false
            },
            onCancelButtonClick = {
                onAction(RecipeDetailsViewModel.OnAction.ShouldDialogAppear(it))
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
            ImageWidget(
                image = recipe.icon
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
                SimpleQuickRecipeInfo(recipe.quantity.toString(), stringResource(R.string.serve)),
                SimpleQuickRecipeInfo(
                    recipe.timeToCreate.toString(),
                    recipe.timeUnit.toString()
                )
            )
        )
    }

}

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
        OneColorBackground(color = MaterialTheme.colorScheme.surface)
        Column {
            TabRow(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 22.dp,
                            topEnd = 22.dp
                        )
                    )
                    .fillMaxWidth(),
                selectedTabIndex = pagerState.settledPage,
                containerColor = MaterialTheme.colorScheme.surface,
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
            Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth(),
                state = pagerState
            ) { pageIndex ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SCREEN_HORIZONTAL_PADDING),
                    verticalArrangement = Arrangement.spacedBy(SMALL_SPACE_BETWEEN_ELEMENTS)
                ) {
                    when (pageIndex) {
                        0 -> {
                            items(
                                items = recipe.ingredientList
                            ) { food ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(
                                                MaterialTheme.colorScheme.primary,
                                                CircleShape
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                                    Text(
                                        modifier = Modifier
                                            .weight(1f),
                                        text = food.title,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = food.quantity.toString(),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Spacer(modifier = Modifier.width(SMALL_SPACE_BETWEEN_ELEMENTS))
                                    Text(
                                        text = food.unitOfMeasurement.toString(),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }

                        1 -> {
                            itemsIndexed(
                                items = recipe.howToSteps,
                                key = { _, item -> item.id!! }
                            ) { index, step ->
                                DetailsRecipeStepItem(
                                    stepNumber = index + 1,
                                    stepTextDescription = step.step
                                )
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
        uiEvent = flow {  },
        onAction = {}
    )
}