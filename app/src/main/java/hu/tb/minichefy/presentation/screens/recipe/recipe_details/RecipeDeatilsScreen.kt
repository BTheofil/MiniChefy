package hu.tb.minichefy.presentation.screens.recipe.recipe_details

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.presentation.preview.RecipePreviewParameterProvider
import hu.tb.minichefy.presentation.screens.components.CircleImage
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.components.DetailsRecipeStepItem
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.components.QuickInfoItem
import java.util.Locale

@Composable
fun RecipeDetailsScreen(
    viewModel: RecipeDetailsViewModel = hiltViewModel()
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
    val screenSize = LocalConfiguration.current

    uiState.recipe?.let {
        BottomSheetScaffold(
            scaffoldState = sheetState,
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = uiState.recipe.title.uppercase(Locale.ROOT),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            sheetPeekHeight = 200.dp,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .heightIn(max = (screenSize.screenHeightDp * 0.6).dp)
                        .padding(horizontal = 16.dp)
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        itemsIndexed(
                            items = uiState.recipe.howToSteps,
                            key = { _, item -> item.id!! }
                        ) { index, recipe ->
                            DetailsRecipeStepItem(
                                stepNumber = index + 1,
                                stepTextDescription = recipe.step
                            )
                        }
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier
            ) {
                CircleImage(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape),
                    image = uiState.recipe.icon
                )
                Spacer(modifier = Modifier.height(64.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .border(
                                1.dp,
                                color = MaterialTheme.colorScheme.tertiary,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        QuickInfoItem(
                            mainDataInfo = uiState.recipe.quantity.toString(),
                            detailsInfo = "serve"
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        QuickInfoItem(
                            mainDataInfo = "25",
                            detailsInfo = "minutes"
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun RecipeDetailsContentPreview(
    @PreviewParameter(RecipePreviewParameterProvider::class) mockRecipe: Recipe
) {
    RecipeDetailsContent(
        uiState = RecipeDetailsViewModel.UiState(
            recipe = mockRecipe
        )
    )
}