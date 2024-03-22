package hu.tb.minichefy.presentation.screens.recipe.recipe_create.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.domain.model.recipe.IngredientDraft
import hu.tb.minichefy.domain.model.recipe.IngredientRecipe
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.presentation.screens.components.SearchItemBar
import hu.tb.minichefy.presentation.ui.components.clickableWithoutRipple
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS
import hu.tb.minichefy.presentation.ui.theme.Red400
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SCREEN_VERTICAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SMALL_SPACE_BETWEEN_ELEMENTS

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IngredientsPage(
    selectedList: List<IngredientRecipe>,
    unselectedList: List<IngredientDraft>,
    queryText: String,
    handleIngredientMovement: (IngredientRecipe) -> Unit,
    onQueryChange: (text: String) -> Unit,
    onSearchClear: () -> Unit,
    onNextClick: () -> Unit,
) {
    var ingredientTitle by remember { mutableStateOf("") }
    var ingredientQuantity by remember { mutableStateOf("") }
    var ingredientUnitOfMeasurement by remember { mutableStateOf(UnitOfMeasurement.PIECE) }
    var unitOfMeasurementTextileWidth by remember { mutableIntStateOf(0) }
    var isDropdownMenuVisible by remember { mutableStateOf(false) }

    var isIngredientTitleHasError by remember { mutableStateOf(false) }
    var isIngredientQuantityHasError by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .clickableWithoutRipple { focusManager.clearFocus() },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(
                        horizontal = SCREEN_HORIZONTAL_PADDING,
                        vertical = SCREEN_VERTICAL_PADDING
                    )
            ) {
                SearchItemBar(
                    queryText = queryText,
                    onQueryChange = onQueryChange,
                    clearIconButtonClick = {
                        if (queryText.isBlank()) {
                            focusManager.clearFocus()
                        }
                        onSearchClear()
                    }
                )
                Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                LazyColumn(
                    modifier = Modifier
                        .height(LocalConfiguration.current.screenHeightDp.dp / 2),
                ) {
                    item(
                        key = "selected_ingredients_title_key"
                    ) {
                        Text(
                            modifier = Modifier.animateItemPlacement(),
                            text = "Selected ingredients",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (selectedList.isEmpty()) {
                        item(
                            key = "empty_ingredient_list_title_key"
                        ) {
                            ListItem(headlineContent = {
                                Text(
                                    text = "No ingredients...",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            })
                        }
                    }
                    items(
                        items = selectedList,
                        key = { item -> item.id ?: item.hashCode() },
                    ) { product ->
                        ListItem(
                            modifier = Modifier.animateItemPlacement(),
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
                                    handleIngredientMovement(product)
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
                            modifier = Modifier.animateItemPlacement()
                        ) {
                            Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                            Text(
                                text = "All ingredients",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    items(
                        items = unselectedList,
                        key = { item -> item.id }
                    ) { ingredient ->
                        ListItem(
                            modifier = Modifier.animateItemPlacement(),
                            headlineContent = {
                                Text(
                                    text = ingredient.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            },
                            trailingContent = {
                                IconButton(onClick = {
                                    ingredientTitle = ingredient.title
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
                Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = ingredientTitle,
                            onValueChange = { value -> ingredientTitle = value },
                            label = {
                                Text(
                                    text = "Ingredient name",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            isError = isIngredientTitleHasError
                        )
                        Spacer(modifier = Modifier.height(SMALL_SPACE_BETWEEN_ELEMENTS))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .weight(1f),
                                value = ingredientQuantity,
                                onValueChange = { value -> ingredientQuantity = value },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                label = {
                                    Text(
                                        text = "Amount",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                },
                                singleLine = true,
                                isError = isIngredientQuantityHasError
                            )
                            Spacer(modifier = Modifier.width(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .onGloballyPositioned { coordinates ->
                                            unitOfMeasurementTextileWidth = coordinates.size.width
                                        },
                                    value = ingredientUnitOfMeasurement.name,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = {
                                        Text(
                                            text = "Measurement",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { isDropdownMenuVisible = true }) {
                                            Icon(
                                                modifier = Modifier,
                                                imageVector = Icons.Rounded.MoreVert,
                                                contentDescription = "measurements menu icon"
                                            )
                                        }
                                    },
                                )
                                DropdownMenu(
                                    modifier = Modifier
                                        .width(with(LocalDensity.current) { unitOfMeasurementTextileWidth.toDp() }),
                                    expanded = isDropdownMenuVisible,
                                    onDismissRequest = { isDropdownMenuVisible = false }) {
                                    UnitOfMeasurement.entries.forEach {
                                        DropdownMenuItem(
                                            text = { Text(text = it.name) },
                                            onClick = {
                                                ingredientUnitOfMeasurement = it
                                                isDropdownMenuVisible = false
                                            })
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(SMALL_SPACE_BETWEEN_ELEMENTS))
                    Button(
                        modifier = Modifier,
                        onClick = {
                            focusManager.clearFocus()

                            if (ingredientTitle.isBlank()) {
                                isIngredientTitleHasError = true
                                return@Button
                            }
                            isIngredientTitleHasError = false
                            if (ingredientQuantity.isBlank()) {
                                isIngredientQuantityHasError = true
                                return@Button
                            }
                            isIngredientQuantityHasError = false

                            handleIngredientMovement(
                                IngredientRecipe(
                                    title = ingredientTitle,
                                    quantity = ingredientQuantity.toFloat(),
                                    unitOfMeasurement = ingredientUnitOfMeasurement
                                )
                            )
                        }) {
                        Text(text = "add")
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onNextClick
                    ) {
                        Text(
                            text = "Next",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun IngredientsPagePreview() {

    var queryText by remember {
        mutableStateOf("")
    }

    IngredientsPage(
        queryText = queryText,
        onQueryChange = { queryText = it },
        onSearchClear = { queryText = "" },
        unselectedList = listOf(IngredientDraft(0, "apple"), IngredientDraft(1, "banana")),
        selectedList = listOf(IngredientRecipe(null, "lemon", 1f, UnitOfMeasurement.PIECE)),
        onNextClick = {},
        handleIngredientMovement = {},
    )
}