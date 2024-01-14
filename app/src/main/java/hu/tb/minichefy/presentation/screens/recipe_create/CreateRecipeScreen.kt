package hu.tb.minichefy.presentation.screens.recipe_create

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.presentation.screens.recipe_create.pages.BasicInformationPage
import hu.tb.minichefy.presentation.screens.recipe_create.pages.StepsPage
import hu.tb.minichefy.presentation.screens.recipe_create.CreateRecipeViewModel.UiEvent
import hu.tb.minichefy.presentation.screens.recipe_create.CreateRecipeViewModel.OnEvent

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
                UiEvent.PageChange -> pager.animateScrollToPage(uiState.targetPageIndex)
                UiEvent.OnRecipeCreateFinish -> onFinishRecipeButtonClick()
            }
        }
    }

    LaunchedEffect(key1 = pager.settledPage){
        viewModel.onEvent(OnEvent.PageChange(pager.settledPage))
    }

    BackHandler(
        enabled = uiState.targetPageIndex != 0
    ) {
        viewModel.onEvent(OnEvent.PageChange(uiState.targetPageIndex - 1))
    }

    HorizontalPager(state = pager) { pageIndex ->
        when (uiState.pages[pageIndex]) {
            is CreateRecipeViewModel.Pages.BasicInformationPage ->
                BasicInformationPage(
                    recipeName = basicPageState.recipeName,
                    counterDisplayContent = basicPageState.quantityCounter,
                    onAddQuantityClick = { viewModel.onEvent(OnEvent.OnQuantityChange(+1)) },
                    onRemoveQuantityClick = { viewModel.onEvent(OnEvent.OnQuantityChange(-1)) },
                    onTitleValueChange = { viewModel.onEvent(OnEvent.OnRecipeTitleChange(it)) },
                    onNextPageClick = { viewModel.onEvent(OnEvent.PageChange(1)) },
                    isQuantityHasError = basicPageState.isQuantityHasError
                )

            is CreateRecipeViewModel.Pages.StepsPage ->
                StepsPage(
                    uiState = stepsPageState,
                    onDeleteItemClick = { viewModel.onEvent(OnEvent.OnDeleteRecipeStep(it)) },
                    onStepTextFieldValueChange = { viewModel.onEvent(OnEvent.OnStepsFieldChange(it)) },
                    onAddItemClick = { viewModel.onEvent(OnEvent.OnAddRecipeStep(stepsPageState.typeField)) },
                    onNextButtonClick = { viewModel.onEvent(OnEvent.OnRecipeSave) }
                )
        }
    }
}

