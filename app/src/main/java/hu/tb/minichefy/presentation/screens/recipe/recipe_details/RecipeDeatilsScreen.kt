package hu.tb.minichefy.presentation.screens.recipe.recipe_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.recipe.SimpleQuickRecipeInfo
import hu.tb.minichefy.presentation.preview.RecipePreviewParameterProvider
import hu.tb.minichefy.presentation.screens.components.icons.iconVectorResource
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.components.ConfirmRecipeAddToStorageDialog
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.components.DetailsRecipeStepItem
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.components.OneColorBackground
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.components.QuickInfoBox
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SMALL_SPACE_BETWEEN_ELEMENTS

@Composable
fun RecipeDetailsScreen(
    viewModel: RecipeDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RecipeDetailsContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun RecipeDetailsContent(
    uiState: RecipeDetailsViewModel.UiState,
    onEvent: (RecipeDetailsViewModel.OnEvent) -> Unit,
) {
    val screenSize = LocalConfiguration.current

    var isConfirmDialogVisible by remember {
        mutableStateOf(false)
    }

    OneColorBackground()

    uiState.recipe?.let { recipe ->
        Column(
            modifier = Modifier
                .padding(horizontal = SCREEN_HORIZONTAL_PADDING)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(DpSize(Dp.Infinity, (screenSize.screenHeightDp.dp / 3))),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    imageVector = iconVectorResource(iconResource = uiState.recipe.icon),
                    contentDescription = "recipe image"
                )
            }
            Spacer(modifier = Modifier.height(SMALL_SPACE_BETWEEN_ELEMENTS))
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = recipe.title,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
            QuickInfoBox(infoList = uiState.recipeQuickInfoList)
            Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(SMALL_SPACE_BETWEEN_ELEMENTS)
            ) {
                itemsIndexed(recipe.howToSteps) { index, step ->
                    DetailsRecipeStepItem(
                        stepNumber = index + 1,
                        stepTextDescription = step.step
                    )
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = {
                    if (uiState.isInformDialogShouldShow) {
                        isConfirmDialogVisible = true
                    } else {
                        onEvent(RecipeDetailsViewModel.OnEvent.MakeRecipe)
                    }
                }
                ) {
                    Text(text = "Make recipe")
                }
            }
        }
    }

    if (isConfirmDialogVisible && uiState.isInformDialogShouldShow) {
        ConfirmRecipeAddToStorageDialog(
            onConfirmButtonClick = {
                onEvent(RecipeDetailsViewModel.OnEvent.ShouldDialogAppear(it))
                onEvent(RecipeDetailsViewModel.OnEvent.MakeRecipe)
                isConfirmDialogVisible = false
            },
            onCancelButtonClick = {
                onEvent(RecipeDetailsViewModel.OnEvent.ShouldDialogAppear(it))
                isConfirmDialogVisible = false
            }
        )
    }
}

@Preview
@Composable
fun RecipeDetailsContentPreview(
    @PreviewParameter(RecipePreviewParameterProvider::class) mockRecipe: Recipe
) {
    RecipeDetailsContent(
        uiState = RecipeDetailsViewModel.UiState(
            recipe = mockRecipe,
            recipeQuickInfoList = listOf(
                SimpleQuickRecipeInfo(mockRecipe.quantity.toString(), "serve"),
                SimpleQuickRecipeInfo(
                    mockRecipe.timeToCreate.toInt().toString(),
                    mockRecipe.timeUnit.toString()
                )
            )
        ),
        onEvent = {}
    )
}