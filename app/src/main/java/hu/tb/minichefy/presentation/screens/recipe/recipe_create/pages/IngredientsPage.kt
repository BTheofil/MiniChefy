package hu.tb.minichefy.presentation.screens.recipe.recipe_create.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import hu.tb.minichefy.domain.model.recipe.IngredientSimple
import hu.tb.minichefy.domain.model.recipe.RecipeIngredient
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.presentation.preview.ProductPreviewParameterProvider
import hu.tb.minichefy.presentation.screens.components.PlusFAB
import hu.tb.minichefy.presentation.screens.components.SearchItemBar
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.ingredient.CreateNewIngredientDialog
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SCREEN_VERTICAL_PADDING

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IngredientsPage(
    selectedList: List<RecipeIngredient>,
    unselectedList: List<IngredientSimple>,
    queryText: String,
    onQueryChange: (text: String) -> Unit,
    onSearchClear: () -> Unit,
    onNextClick: () -> Unit,
    moveIngredientPosition: (ingredient: RecipeIngredient) -> Unit,
) {
    var isIngredientDialogVisible by remember {
        mutableStateOf(false)
    }
    var optionalParameterPass by remember {
        mutableStateOf<RecipeIngredient?>(null)
    }

    Scaffold(
        floatingActionButton = {
            PlusFAB(onClick = {
                optionalParameterPass = null
                isIngredientDialogVisible = true
            })
        },
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
                    clearIconButtonClick = onSearchClear
                )
                LazyColumn {
                    if (selectedList.isNotEmpty()) {
                        item(
                            key = "selected_ingredients_title_key"
                        ) {
                            Text(
                                modifier = Modifier.animateItemPlacement(),
                                text = "Selected ingredients",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    items(
                        items = selectedList,
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
                                Text(
                                    text = product.quantity.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            },
                            leadingContent = {
                                Checkbox(checked = true, onCheckedChange = {
                                    moveIngredientPosition(product)
                                })
                            },
                            trailingContent = {
                                IconButton(onClick = {
                                    optionalParameterPass = product
                                    isIngredientDialogVisible = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Edit,
                                        contentDescription = "edit selected ingredient icon"
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
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    items(
                        items = unselectedList,
                        key = { item -> item.id }
                    ) { product ->
                        ListItem(
                            modifier = Modifier.animateItemPlacement(),
                            leadingContent = {
                                Checkbox(checked = false, onCheckedChange = {
                                    optionalParameterPass = product
                                    isIngredientDialogVisible = true
                                })
                            },
                            headlineContent = {
                                Text(
                                    text = product.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            })
                    }
                }
                Button(onClick = onNextClick) {
                    Text(text = "Next")
                }
            }
        }
    )

    if (isIngredientDialogVisible) {
        CreateNewIngredientDialog(
            ingredient = optionalParameterPass,
            onDismissRequest = { isIngredientDialogVisible = false },
            onCancelClick = { isIngredientDialogVisible = false },
            onProceedClick = {
                moveIngredientPosition(it)
                isIngredientDialogVisible = false
            }
        )
    }
}

@Preview
@Composable
private fun IngredientsPagePreview(
    @PreviewParameter(ProductPreviewParameterProvider::class) foodList: List<Food>
) {

    var queryText by remember {
        mutableStateOf("")
    }

    IngredientsPage(
        queryText = queryText,
        onQueryChange = { queryText = it },
        onSearchClear = { queryText = "" },
        unselectedList = emptyList(),
        selectedList = emptyList(),
        moveIngredientPosition = {},
        onNextClick = {},
    )
}