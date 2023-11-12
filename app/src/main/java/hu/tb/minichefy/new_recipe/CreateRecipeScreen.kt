package hu.tb.minichefy.new_recipe

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.tb.minichefy.new_recipe.pages.BasicInformationPage
import hu.tb.minichefy.new_recipe.pages.StepsPage
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateRecipe(
    viewModel: CreateRecipeViewModel = viewModel(),
    onFinishRecipeButtonClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val stepsPageState by viewModel.stepsPageState.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    LaunchedEffect(key1 = stepsPageState.recipeSteps) {
        if (stepsPageState.recipeSteps.isNotEmpty()) {
            listState.animateScrollToItem(stepsPageState.recipeSteps.lastIndex)
        }
    }

    val pager = rememberPagerState {
        uiState.pages.size
    }

    HorizontalPager(state = pager) { pageIndex ->
        when (uiState.pages[pageIndex]) {
            is CreateRecipeViewModel.Pages.BasicInformationPage ->

                BasicInformationPage(
                    recipeName = stepsPageState.typeField,
                    onAddQuantityClick = { viewModel.onQuantityChange(1) },
                    onRemoveQuantityClick = { viewModel.onQuantityChange(-1) },
                    onGiveTitleValueChange = {},
                    onNextPageClick = { pager.scrollToPage(1) }
                )
            is CreateRecipeViewModel.Pages.StepsPage ->
                StepsPage(
                    uiState = stepsPageState,
                    onDeleteItemClick = {
                        viewModel.removeRecipeStep(it)
                    },
                    onStepTextFieldValueChange = {
                        viewModel.onFieldChange(it)
                    },
                    onAddItemIconClick = {
                        viewModel.addRecipeStep(stepsPageState.typeField)
                    },
                    onNextButtonClick = onFinishRecipeButtonClick
                )
        }
    }
}

