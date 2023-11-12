package hu.tb.minichefy.recipe_list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel = viewModel(),
    onFloatingButtonClick: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onFloatingButtonClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "create new recipe")
            }
        },
        content = {
            LazyColumn(
                modifier = Modifier.padding(it)
            ) {
                items(
                    items = uiState.recipeList
                ) { recipe ->

                }
            }
        })
}