package hu.tb.minichefy.presentation.screens.recipe.recipe_create

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.CreateRecipeViewModel.OnBasicInformationPageEvent
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.CreateRecipeViewModel.OnIngredientEvent
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.CreateRecipeViewModel.OnStepsPageEvent
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.CreateRecipeViewModel.UiEvent
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.pages.BasicInformationPage
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.pages.IngredientsPage
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.pages.StepsPage
import kotlinx.coroutines.launch

private const val BASIC_INFORMATION_PAGE_INDEX = 0
private const val INGREDIENTS_PAGE_INDEX = 1
private const val RECIPE_STEPS_PAGE_INDEX = 2

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

    var isRecipeTitleHasError by remember {
        mutableStateOf(false)
    }
    var isRecipeTimeHasError by remember {
        mutableStateOf(false)
    }
    val pager = rememberPagerState {
        uiState.pages.size
    }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ErrorInRecipeFields -> {
                    isRecipeTitleHasError = event.isRecipeTitleHasError
                    isRecipeTimeHasError = event.isRecipeTimeHasError
                    if (event.isRecipeTitleHasError || event.isRecipeTimeHasError) {
                        pager.animateScrollToPage(BASIC_INFORMATION_PAGE_INDEX)
                    }
                    scope.launch {
                        showAppropriateSnackBar(event, snackBarHostState)
                    }
                }

                UiEvent.RecipeSaved -> onFinishRecipeButtonClick()
            }
        }
    }

    LaunchedEffect(pager.settledPage) {
        scope.launch {
            pager.animateScrollToPage(pager.settledPage)
        }
    }

    BackHandler(
        enabled = pager.settledPage != BASIC_INFORMATION_PAGE_INDEX
    ) {
        scope.launch {
            pager.animateScrollToPage(pager.settledPage - 1)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddingValues ->
        HorizontalPager(
            modifier = Modifier
                .padding(paddingValues),
            state = pager
        ) { pageIndex ->
            when (uiState.pages[pageIndex]) {
                is CreateRecipeViewModel.Pages.BasicInformationPage ->
                    BasicInformationPage(
                        uiState = basicPageState,
                        isRecipeTitleHasError = isRecipeTitleHasError,
                        isRecipeTimeHasError = isRecipeTimeHasError,
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
                        onTimeFieldValueChange = {
                            viewModel.onBasicInformationPageEvent(
                                OnBasicInformationPageEvent.OnTimeChange(it)
                            )
                        },
                        onTimeUnitValueChange = {
                            viewModel.onBasicInformationPageEvent(
                                OnBasicInformationPageEvent.OnTimeUnitChange(it)
                            )
                        },
                        onNextPageClick = {
                            scope.launch {
                                pager.animateScrollToPage(
                                    INGREDIENTS_PAGE_INDEX
                                )
                            }
                        }
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
                        onNextButtonClick = {
                            scope.launch { pager.animateScrollToPage(RECIPE_STEPS_PAGE_INDEX) }
                        }
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
                            viewModel.onStepsPageEvent(
                                OnStepsPageEvent.OnStepsFieldChange(
                                    text,
                                    index
                                )
                            )
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
}

private suspend fun showAppropriateSnackBar(
    event: UiEvent.ErrorInRecipeFields,
    snackBarHostState: SnackbarHostState
) {
    when {
        event.isIngredientHasError && event.isStepsHasError -> {
            snackBarHostState.showSnackbar("Missing ingredients & steps! Add them to get cooking.")
        }

        event.isIngredientHasError -> {
            snackBarHostState.showSnackbar("You haven't added any ingredients yet.")
        }

        event.isStepsHasError -> {
            snackBarHostState.showSnackbar("You're missing some recipe steps.")
        }
    }
}

