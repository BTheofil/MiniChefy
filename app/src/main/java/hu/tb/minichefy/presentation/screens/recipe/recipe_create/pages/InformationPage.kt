package hu.tb.minichefy.presentation.screens.recipe.recipe_create.pages

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.presentation.screens.components.icons.FoodIcon
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.CreateRecipeViewModel
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.info.CircleImage
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.info.IconSelectorSheet
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.info.QuestionForm
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationPage(
    uiState: CreateRecipeViewModel.Pages.BasicInformationPage,
    onTitleValueChange: (String) -> Unit,
    onRemoveQuantityClick: () -> Unit,
    onAddQuantityClick: () -> Unit,
    onNextPageClick: () -> Unit,
    onSelectedIconClick: (icon: FoodIcon) -> Unit
) {
    val focusManager = LocalFocusManager.current

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showIconPicker by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .padding(horizontal = 22.dp)
            .padding(top = 22.dp)
    ) {
        CircleImage(
            image = uiState.selectedFoodIcon.resource,
            borderWidth = 2.dp,
            borderColor = MaterialTheme.colorScheme.primary,
            borderShape = CircleShape,
            onClick = {
                scope.launch {
                    showIconPicker = true
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        QuestionForm(
            uiState.recipeName,
            onTitleValueChange,
            onAddQuantityClick = {
                focusManager.clearFocus()
                onAddQuantityClick()
            },
            onRemoveQuantityClick = {
                focusManager.clearFocus()
                onRemoveQuantityClick()
            },
            quantityContent = uiState.quantityCounter,
            isErrorHappened = uiState.isQuantityHasError
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            OutlinedButton(
                modifier = Modifier,
                onClick = {
                    focusManager.clearFocus()
                    onNextPageClick()
                }
            ) {
                Text(
                    text = "Next page",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        if (showIconPicker) {
            IconSelectorSheet(
                sheetState = sheetState,
                foodIconList = uiState.defaultIconCollection,
                selectedFoodIcon = uiState.selectedFoodIcon,
                onItemClick = {
                    onSelectedIconClick(it)
                },
                onDismissRequest = { showIconPicker = false }
            )
        }
    }
}

@Preview
@Composable
fun InformationPagePreview() {
    InformationPage(CreateRecipeViewModel.Pages.BasicInformationPage(), {}, {}, {}, {}, {})
}