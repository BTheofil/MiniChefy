package hu.tb.minichefy.presentation.screens.recipe.recipe_create.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.presentation.preview.ProductPreviewParameterProvider
import hu.tb.minichefy.presentation.screens.components.SearchItemBar
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SCREEN_VERTICAL_PADDING

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IngredientsPage(
    allIngredients: List<Food>,
    onProductClick: (product: Food) -> Unit,
    queryText: String,
    onQueryChange: (text: String) -> Unit,
    onSearchClear: () -> Unit,
    onNextClick: () -> Unit
) {
    Column(
        modifier = Modifier
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
            items(allIngredients,
                key = { item -> item.id!! }) { product ->
                Row(
                    modifier = Modifier
                        .animateItemPlacement(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = false, onCheckedChange = { onProductClick(product) })
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Button(onClick = onNextClick) {
            Text(text = "Next")
        }
    }
}

@Preview
@Composable
private fun IngredientsPagePreview(
    @PreviewParameter(ProductPreviewParameterProvider::class) productList: List<Food>
) {

    var queryText by remember {
        mutableStateOf("")
    }

    IngredientsPage(
        queryText = queryText,
        onQueryChange = { queryText = it },
        onSearchClear = { queryText = "" },
        allIngredients = productList,
        onProductClick = {},
        onNextClick = {}
    )
}