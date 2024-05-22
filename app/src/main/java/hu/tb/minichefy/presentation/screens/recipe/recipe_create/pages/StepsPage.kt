package hu.tb.minichefy.presentation.screens.recipe.recipe_create.pages

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.rememberTransition
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import hu.tb.minichefy.R
import hu.tb.minichefy.domain.model.recipe.RecipeStep
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.CreateRecipeViewModel
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.steps.RecipeStepItem
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SCREEN_VERTICAL_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepsPage(
    uiState: CreateRecipeViewModel.Pages.StepsPage,
    onStepTextFieldValueChange: (text: String, index: Int) -> Unit,
    onDeleteItemClick: (Int) -> Unit,
    onRecipeItemClick: (index: Int) -> Unit,
    onAddButtonClick: () -> Unit,
    onNextButtonClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyBoardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = stringResource(R.string.write_down_the_recipe_steps))
            })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .padding(
                    horizontal = SCREEN_HORIZONTAL_PADDING,
                    vertical = SCREEN_VERTICAL_PADDING
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(
                    items = uiState.recipeSteps,
                ) { index, item ->

                    val appearContent = remember {
                        MutableTransitionState(false)
                    }
                    appearContent.targetState = true

                    RecipeStepItem(
                        index = index,
                        displayText = item.step,
                        closeIconVisible = uiState.recipeSteps.size > 1,
                        onRecipeStepTextFieldChange = { text ->
                            onStepTextFieldValueChange(text, index)
                        },
                        onDeleteItemClick = onDeleteItemClick,
                        onRecipeItemClick = onRecipeItemClick,
                        keyboardController = keyBoardController,
                        showContentAnimation = rememberTransition(transitionState = appearContent),
                        showLineAnimation = uiState.recipeSteps.last() !== item
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                    Button(onClick = {
                        onAddButtonClick()
                    }) {
                        Text(text = stringResource(R.string.add_more_step))
                    }
                }
            }
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = onNextButtonClick
            ) {
                Text(
                    text = stringResource(R.string.done),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview
@Composable
fun StepsPagePreview() {
    val testList = remember {
        mutableStateListOf(RecipeStep(1, "first"))
    }

    StepsPage(
        CreateRecipeViewModel.Pages.StepsPage(
            recipeSteps = testList
        ),
        onNextButtonClick = {},
        onAddButtonClick = {
            testList.add(RecipeStep(testList.size + 1L, "aaa"))
        },
        onStepTextFieldValueChange = { _, _ -> },
        onDeleteItemClick = {
            testList.removeAt(it)
        },
        onRecipeItemClick = {}
    )
}