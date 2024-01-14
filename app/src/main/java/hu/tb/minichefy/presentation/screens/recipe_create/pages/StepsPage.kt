package hu.tb.minichefy.presentation.screens.recipe_create.pages

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.domain.model.RecipeStep
import hu.tb.minichefy.presentation.screens.recipe_create.CreateRecipeViewModel
import hu.tb.minichefy.presentation.screens.recipe_create.components.RecipeStepItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepsPage(
    uiState: CreateRecipeViewModel.Pages.StepsPage,
    onDeleteItemClick: (Int) -> Unit,
    onStepTextFieldValueChange: (String) -> Unit,
    onAddItemClick: () -> Unit,
    onNextButtonClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = "Write down the recipe steps")
            })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding() + 16.dp)
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
                        index = index + 1,
                        displayText = item.step,
                        onDeleteItemClick = onDeleteItemClick,
                        onRecipeStepTextFieldChange = onStepTextFieldValueChange
                    )
                }
                item {
                    RecipeStepItem(
                        index = uiState.recipeSteps.size + 1,
                        displayText = uiState.typeField,
                        onDeleteItemClick = {},
                        onRecipeStepTextFieldChange = onStepTextFieldValueChange
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onAddItemClick) {
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
        onAddItemClick = {},
        onStepTextFieldValueChange = {},
        onDeleteItemClick = {}
    )
}