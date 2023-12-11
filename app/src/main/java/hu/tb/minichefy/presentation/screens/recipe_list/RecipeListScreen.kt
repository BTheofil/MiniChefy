package hu.tb.minichefy.presentation.screens.recipe_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.presentation.screens.recipe_list.components.RecipeItem

@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel = hiltViewModel(),
    onFloatingButtonClick: () -> Unit,
    onItemClick: (Long) -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is RecipeListViewModel.UiEvent.OnItemClick -> onItemClick(event.recipeId)
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onFloatingButtonClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "create new recipe")
            }
        },
        content = {
            LazyVerticalGrid(
                modifier = Modifier.padding(it),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                this.items(
                    items = uiState.recipeList,
                    key = { item -> item.id!! }
                ) { recipe ->
                    RecipeItem(
                        title = recipe.name,
                        onItemClick = { viewModel.onItemClick(recipe.id!!) }
                    )
                }
            }
        })
}

@Preview
@Composable
fun RecipeListScreenPreview() {
    RecipeListScreen(
        onFloatingButtonClick = {},
        onItemClick = {}
    )
}