package hu.tb.minichefy.presentation.screens.recipe_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.domain.model.Recipe
import hu.tb.minichefy.domain.model.RecipeStep
import hu.tb.minichefy.presentation.screens.recipe_create.components.RecipeStepItem
import java.util.Locale

@Composable
fun RecipeDetailsScreen(
    viewModel: RecipeDetailsViewModel = hiltViewModel(),
    navigateToRecipeList: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RecipeDetailsContent(uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsContent(
    uiState: RecipeDetailsViewModel.UiState
) {
    val sheetState = rememberBottomSheetScaffoldState()

    LaunchedEffect(Unit){
        sheetState.bottomSheetState.show()
    }

    uiState.recipe?.let {
        BottomSheetScaffold(
            scaffoldState = sheetState,
            topBar = {
                CenterAlignedTopAppBar(title = {
                    Text(
                        text = uiState.recipe.name.uppercase(Locale.ROOT),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                })
            },
            sheetContent = {
                LazyColumn {
                    itemsIndexed(
                        items = uiState.recipe.howToSteps,
                        key = { _, item -> item.id!! }
                    ) { index, recipe ->
                        RecipeStepItem(index = index, item = recipe.step, onDeleteItemClick = {})
                    }
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.tertiary),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .padding(16.dp),
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "recipe image"
                )
            }
        }
    }
}

@Preview
@Composable
fun RecipeDetailsContentPreview() {
    RecipeDetailsContent(
        uiState = RecipeDetailsViewModel.UiState(
            recipe = Recipe(
                id = 0,
                name = "test",
                quantity = 1,
                howToSteps = listOf(
                    RecipeStep(0, "first"),
                    RecipeStep(1, "second")
                )
            )
        )
    )
}