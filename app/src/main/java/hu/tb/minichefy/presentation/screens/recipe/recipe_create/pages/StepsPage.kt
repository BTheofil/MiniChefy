package hu.tb.minichefy.presentation.screens.recipe.recipe_create.pages

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.domain.model.recipe.RecipeStep
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.CreateRecipeViewModel
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.steps.RecipeStepItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
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
                Text(text = "Write down the recipe steps")
            })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding() + 16.dp)
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(items = uiState.recipeSteps) { index, item ->
                    RecipeStepItem(
                        index = index,
                        displayText = item.step,
                        closeIconVisible = uiState.recipeSteps.size > 1,
                        onRecipeStepTextFieldChange = { text ->
                            onStepTextFieldValueChange(text, index)
                        },
                        onDeleteItemClick = onDeleteItemClick,
                        onRecipeItemClick = onRecipeItemClick,
                        keyboardController = keyBoardController
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        onAddButtonClick()
                    }) {
                        Text(text = "Add more step")
                    }
                }
            }
            OutlinedButton(onClick = onNextButtonClick) {
                Text(text = "Finish")
            }
            Spacer(modifier = Modifier.height(22.dp))
        }
    }
}

@Preview
@Composable
fun StepsPagePreview() {
    StepsPage(
        CreateRecipeViewModel.Pages.StepsPage(
            recipeSteps = listOf(RecipeStep(1, "first"))
        ),
        onNextButtonClick = {},
        onAddButtonClick = {},
        onStepTextFieldValueChange = { _, _ -> },
        onDeleteItemClick = {},
        onRecipeItemClick = {}
    )
}