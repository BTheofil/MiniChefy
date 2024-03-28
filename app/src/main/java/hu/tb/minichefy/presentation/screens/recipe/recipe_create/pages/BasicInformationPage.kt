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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.domain.model.recipe.TimeUnit
import hu.tb.minichefy.presentation.screens.components.CircleImage
import hu.tb.minichefy.presentation.screens.manager.icons.MealIcon
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.CreateRecipeViewModel
import hu.tb.minichefy.presentation.screens.components.IconSelectorSheet
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.PageNextButton
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.info.QuestionForm
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SCREEN_VERTICAL_PADDING

@Composable
fun BasicInformationPage(
    uiState: CreateRecipeViewModel.Pages.BasicInformationPage,
    onTitleValueChange: (String) -> Unit,
    onRemoveQuantityClick: () -> Unit,
    onAddQuantityClick: () -> Unit,
    onNextPageClick: () -> Unit,
    onSelectedIconClick: (icon: MealIcon) -> Unit,
    onTimeFieldValueChange: (String) -> Unit,
    onTimeUnitValueChange: (TimeUnit) -> Unit
) {
    val focusManager = LocalFocusManager.current

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
            .padding(
                horizontal = SCREEN_HORIZONTAL_PADDING,
                vertical = SCREEN_VERTICAL_PADDING
            )
    ) {
        CircleImage(
            image = uiState.selectedMealIcon.resource,
            borderWidth = 2.dp,
            borderColor = MaterialTheme.colorScheme.primary,
            borderShape = CircleShape,
            onClick = { showIconPicker = true }
        )
        Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
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
            isErrorHappened = uiState.isQuantityHasError,
            timeFieldValue = uiState.timeField,
            timeFieldValueChange = onTimeFieldValueChange,
            timeUnitValue = uiState.timeUnit,
            timeUnitValueChange = onTimeUnitValueChange
        )
        Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            PageNextButton(
                text = "Next page",
                onClick = {
                    focusManager.clearFocus()
                    onNextPageClick()
                }
            )
        }
        if (showIconPicker) {
            IconSelectorSheet(
                allIconList = uiState.defaultIconCollection,
                selectedIcon = uiState.selectedMealIcon,
                onItemClick = {
                    onSelectedIconClick(it as MealIcon)
                },
                onDismissRequest = { showIconPicker = false }
            )
        }
    }
}

@Preview
@Composable
fun InformationPagePreview() {
    BasicInformationPage(
        CreateRecipeViewModel.Pages.BasicInformationPage(),
        {},
        {},
        {},
        {},
        {},
        {},
        {}
    )
}