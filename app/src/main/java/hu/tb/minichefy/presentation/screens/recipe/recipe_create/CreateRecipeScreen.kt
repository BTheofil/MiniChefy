package hu.tb.minichefy.presentation.screens.recipe.recipe_create

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.pages.BasicInformationPage
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.pages.StepsPage
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.pages.IngredientsPage
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.CreateRecipeViewModel.OnIngredientEvent
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.CreateRecipeViewModel.OnBasicInformationPageEvent
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.CreateRecipeViewModel.OnStepsPageEvent
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.CreateRecipeViewModel.UiEvent
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.CreateRecipeViewModel.OnEvent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateRecipe(
    viewModel: CreateRecipeViewModel = hiltViewModel(),
    onFinishRecipeButtonClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val basicPageState by viewModel.basicPageState.collectAsStateWithLifecycle()
    val ingredientsPageState by viewModel.ingredientsPageState.collectAsStateWithLifecycle()
    val stepsPageState by viewModel.stepsPageState.collectAsStateWithLifecycle()

    val pager = rememberPagerState {
        uiState.pages.size
    }
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                UiEvent.PageChange -> pager.animateScrollToPage(uiState.targetPageIndex)
            }
        }
    }

    LaunchedEffect(key1 = pager.settledPage) {
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
                    uiState = basicPageState,
                    onAddQuantityClick = {
                        viewModel.onBasicInformationPageEvent(
                            OnBasicInformationPageEvent.OnQuantityChange(+1)
                        )
                    },
                    onRemoveQuantityClick = {
                        viewModel.onBasicInformationPageEvent(
                            OnBasicInformationPageEvent.OnQuantityChange(-1)
                        )
                    },
                    onTitleValueChange = {
                        viewModel.onBasicInformationPageEvent(
                            OnBasicInformationPageEvent.OnRecipeTitleChange(it)
                        )
                    },
                    onSelectedIconClick = {
                        viewModel.onBasicInformationPageEvent(
                            OnBasicInformationPageEvent.OnSelectedIconChange(it)
                        )
                    },
                    onNextPageClick = { viewModel.onEvent(OnEvent.PageChange(1)) }
                )

            is CreateRecipeViewModel.Pages.IngredientsPage ->
                IngredientsPage(
                    uiState = ingredientsPageState,
                    onQueryChange = {
                        viewModel.onIngredientPageEvent(
                            OnIngredientEvent.OnSearchValueChange(
                                it
                            )
                        )
                    },
                    onSearchClear = {
                        viewModel.onIngredientPageEvent(
                            OnIngredientEvent.OnSearchValueChange(
                                ""
                            )
                        )
                    },
                    onAddIngredientClick = {
                        viewModel.onIngredientPageEvent(OnIngredientEvent.IngredientAdd)
                    },
                    onRemoveIngredientClick = {
                        viewModel.onIngredientPageEvent(OnIngredientEvent.IngredientRemove(it))
                    },
                    onIngredientTitleChange = {
                        viewModel.onIngredientPageEvent(
                            OnIngredientEvent.OnIngredientTitleChange(
                                it
                            )
                        )
                    },
                    onIngredientQuantityChange = {
                        viewModel.onIngredientPageEvent(
                            OnIngredientEvent.OnIngredientQuantityChange(
                                it
                            )
                        )
                    },
                    onIngredientUnitOfMeasurementChange = {
                        viewModel.onIngredientPageEvent(
                            OnIngredientEvent.OnIngredientUnitOfMeasurementChange(it)
                        )
                    },
                    onNextButtonClick = { viewModel.onEvent(OnEvent.PageChange(2)) }
                )

            is CreateRecipeViewModel.Pages.StepsPage ->
                StepsPage(
                    uiState = stepsPageState,
                    onDeleteItemClick = {
                        viewModel.onStepsPageEvent(
                            OnStepsPageEvent.OnDeleteRecipeStep(
                                it
                            )
                        )
                    },
                    onStepTextFieldValueChange = { text, index ->
                        viewModel.onStepsPageEvent(OnStepsPageEvent.OnStepsFieldChange(text, index))
                    },
                    onAddButtonClick = {
                        viewModel.onStepsPageEvent(
                            OnStepsPageEvent.OnAddRecipeStepToList(
                                stepsPageState.stepBoxTextField
                            )
                        )
                        viewModel.onStepsPageEvent(OnStepsPageEvent.ClearStepField)
                    },
                    onNextButtonClick = {
                        viewModel.onStepsPageEvent(OnStepsPageEvent.OnRecipeSave)
                        onFinishRecipeButtonClick()
                    },
                    onRecipeItemClick = {
                        viewModel.onStepsPageEvent(
                            OnStepsPageEvent.RecipeItemClick(
                                it
                            )
                        )
                    }
                )
        }
    }
}

