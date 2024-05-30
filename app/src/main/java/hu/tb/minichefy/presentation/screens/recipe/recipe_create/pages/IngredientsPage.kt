package hu.tb.minichefy.presentation.screens.recipe.recipe_create.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import hu.tb.minichefy.R
import hu.tb.minichefy.domain.model.storage.FoodSummary
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.presentation.preview.FoodSummaryPreviewParameterProvider
import hu.tb.minichefy.presentation.screens.components.QuantityAndMeasurementRow
import hu.tb.minichefy.presentation.screens.components.SearchItemBar
import hu.tb.minichefy.presentation.screens.components.extensions.clickableWithoutRipple
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.CreateRecipeViewModel
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.PageNextButton
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS
import hu.tb.minichefy.presentation.ui.theme.Red400
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SCREEN_VERTICAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SMALL_SPACE_BETWEEN_ELEMENTS

@Composable
fun IngredientsPage(
    uiState: CreateRecipeViewModel.Pages.IngredientsPage,
    onIngredientTitleChange: (String) -> Unit,
    onIngredientQuantityChange: (String) -> Unit,
    onIngredientUnitOfMeasurementChange: (UnitOfMeasurement) -> Unit,
    onPreMadeIngredientClick: (title: String, unitOfMeasurement: UnitOfMeasurement) -> Unit,
    onAddIngredientClick: () -> Unit,
    onRemoveIngredientClick: (Int) -> Unit,
    onQueryChange: (text: String) -> Unit,
    onSearchClear: () -> Unit,
    onNextButtonClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .clickableWithoutRipple { focusManager.clearFocus() },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(
                        horizontal = SCREEN_HORIZONTAL_PADDING,
                        vertical = SCREEN_VERTICAL_PADDING
                    )
            ) {
                SearchItemBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    queryText = uiState.searchText,
                    onQueryChange = onQueryChange,
                    clearIconButtonClick = {
                        if (uiState.searchText.isBlank()) {
                            focusManager.clearFocus()
                        }
                        onSearchClear()
                    }
                )
                Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                IngredientList(
                    modifier = Modifier
                        .weight(1f),
                    uiState = uiState,
                    onPreMadeIngredientClick = onPreMadeIngredientClick,
                    onRemoveIngredientClick = onRemoveIngredientClick
                )
                Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                IngredientsCreatePanel(
                    modifier = Modifier,
                    uiState = uiState,
                    onIngredientTitleChange = onIngredientTitleChange,
                    onIngredientQuantityChange = onIngredientQuantityChange,
                    onIngredientUnitOfMeasurementChange = onIngredientUnitOfMeasurementChange,
                    onAddIngredientClick = onAddIngredientClick,
                    onNextButtonClick = onNextButtonClick
                )
            }
        }
    )
}

@Composable
fun IngredientList(
    modifier: Modifier = Modifier,
    uiState: CreateRecipeViewModel.Pages.IngredientsPage,
    onPreMadeIngredientClick: (title: String, unitOfMeasurement: UnitOfMeasurement) -> Unit,
    onRemoveIngredientClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item(
            key = "selected_ingredients_title_key"
        ) {
            Text(
                modifier = Modifier.animateItem(),
                text = stringResource(id = R.string.selected_ingredients),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        if (uiState.selectedIngredientList.isEmpty()) {
            item(
                key = "empty_ingredient_list_title_key"
            ) {
                ListItem(
                    modifier = Modifier.animateItem(),
                    headlineContent = {
                        Text(
                            text = stringResource(id = R.string.no_ingredients),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    })
            }
        }
        itemsIndexed(
            items = uiState.selectedIngredientList,
        ) { index, product ->
            ListItem(
                modifier = Modifier.animateItem(),
                headlineContent = {
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                supportingContent = {
                    Row {
                        Text(
                            text = product.quantity.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(SMALL_SPACE_BETWEEN_ELEMENTS))
                        Text(
                            text = product.unitOfMeasurement.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                trailingContent = {
                    IconButton(onClick = {
                        onRemoveIngredientClick(index)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "remove selected ingredient icon",
                            tint = Color(Red400.value)
                        )
                    }
                })
        }
        item(
            key = "all_ingredients_title_key"
        ) {
            Column(
                modifier = Modifier.animateItem()
            ) {
                Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                Text(
                    text = stringResource(id = R.string.all_ingredients),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        items(
            items = uiState.unSelectedIngredientList,
            key = { item -> item.id }
        ) { ingredient ->
            ListItem(
                modifier = Modifier.animateItem(),
                headlineContent = {
                    Text(
                        text = ingredient.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                trailingContent = {
                    IconButton(onClick = {
                        onPreMadeIngredientClick(ingredient.title, ingredient.unitOfMeasurement)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.AddCircle,
                            contentDescription = "add ingredient icon",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                })
        }
    }
}

@Composable
fun IngredientsCreatePanel(
    modifier: Modifier = Modifier,
    uiState: CreateRecipeViewModel.Pages.IngredientsPage,
    onIngredientTitleChange: (String) -> Unit,
    onIngredientQuantityChange: (String) -> Unit,
    onIngredientUnitOfMeasurementChange: (UnitOfMeasurement) -> Unit,
    onAddIngredientClick: () -> Unit,
    onNextButtonClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier,
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = uiState.ingredientTitleDraft,
            onValueChange = onIngredientTitleChange,
            label = {
                Text(
                    text = stringResource(id = R.string.ingredient_name),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            isError = uiState.isIngredientTitleHasError
        )
        Spacer(modifier = Modifier.height(SMALL_SPACE_BETWEEN_ELEMENTS))
        QuantityAndMeasurementRow(
            quantityValue = uiState.ingredientQuantityDraft,
            onQuantityChange = onIngredientQuantityChange,
            quantityLabel = stringResource(id = R.string.amount),
            isQuantityHasError = uiState.isIngredientQuantityHasError,
            measurementValue = uiState.ingredientUnitOfMeasurementDraft,
            onMeasurementChange = onIngredientUnitOfMeasurementChange,
            measurementLabel = stringResource(id = R.string.measurement),
            measurementOptionList = UnitOfMeasurement.entries
        )
        Spacer(modifier = Modifier.height(SMALL_SPACE_BETWEEN_ELEMENTS))
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                focusManager.clearFocus()
                onAddIngredientClick()
            }) {
            Text(text = stringResource(id = R.string.add_ingredient))
        }
        PageNextButton(onClick = onNextButtonClick)
    }
}

@Preview
@Composable
private fun IngredientsPagePreview(
    @PreviewParameter(FoodSummaryPreviewParameterProvider::class) mockFoodSummaryList: List<FoodSummary>
) {

    var queryText by remember {
        mutableStateOf("")
    }

    IngredientsPage(
        uiState = CreateRecipeViewModel.Pages.IngredientsPage(
            selectedIngredientList = listOf(),
            unSelectedIngredientList = mockFoodSummaryList,
            searchText = queryText,
        ),
        onQueryChange = { queryText = it },
        onSearchClear = { queryText = "" },
        onNextButtonClick = {},
        onAddIngredientClick = {},
        onRemoveIngredientClick = {},
        onIngredientQuantityChange = {},
        onIngredientTitleChange = {},
        onIngredientUnitOfMeasurementChange = {},
        onPreMadeIngredientClick = { _, _ -> }
    )
}