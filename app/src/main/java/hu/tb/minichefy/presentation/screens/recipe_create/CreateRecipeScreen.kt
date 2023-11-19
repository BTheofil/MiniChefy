package hu.tb.minichefy.presentation.screens.recipe_create

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.presentation.screens.recipe_create.pages.BasicInformationPage
import hu.tb.minichefy.presentation.screens.recipe_create.pages.StepsPage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateRecipe(
    viewModel: CreateRecipeViewModel = hiltViewModel(),
    onFinishRecipeButtonClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val stepsPageState by viewModel.stepsPageState.collectAsStateWithLifecycle()
    val basicPageState by viewModel.basicPageState.collectAsStateWithLifecycle()

    val pager = rememberPagerState {
        uiState.pages.size
    }
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                CreateRecipeViewModel.UiEvent.OnNextPageClick -> pager.animateScrollToPage(uiState.targetPageIndex)
                CreateRecipeViewModel.UiEvent.OnPreviousPage -> pager.animateScrollToPage(uiState.targetPageIndex)
            }
        }
    }

    val listState = rememberLazyListState()
    LaunchedEffect(key1 = stepsPageState.recipeSteps) {
        if (stepsPageState.recipeSteps.isNotEmpty()) {
            listState.animateScrollToItem(stepsPageState.recipeSteps.lastIndex)
        }
    }

    BackHandler(
        enabled = uiState.targetPageIndex != 0
    ) {
        viewModel.onPreviousPageBack()
    }

    HorizontalPager(state = pager) { pageIndex ->
        when (uiState.pages[pageIndex]) {
            is CreateRecipeViewModel.Pages.BasicInformationPage ->
                BasicInformationPage(
                    recipeName = basicPageState.recipeName,
                    counterDisplayContent = basicPageState.quantityCounter,
                    onAddQuantityClick = { viewModel.onQuantityChange(1) },
                    onRemoveQuantityClick = { viewModel.onQuantityChange(-1) },
                    onGiveTitleValueChange = {  },
                    onNextPageClick = { viewModel.onNextPageClick() }
                )

            is CreateRecipeViewModel.Pages.StepsPage ->
                StepsPage(
                    uiState = stepsPageState,
                    onDeleteItemClick = { viewModel.removeRecipeStep(it) },
                    onStepTextFieldValueChange = { viewModel.onFieldChange(it) },
                    onAddItemIconClick = { viewModel.addRecipeStep(stepsPageState.typeField) },
                    onNextButtonClick = onFinishRecipeButtonClick
                )
        }
    }
}

