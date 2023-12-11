package hu.tb.minichefy.presentation.screens.recipe_create.pages

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.domain.model.RecipeStep
import hu.tb.minichefy.presentation.screens.recipe_create.CreateRecipeViewModel
import hu.tb.minichefy.presentation.screens.recipe_create.components.RecipeStepItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StepsPage(
    uiState: CreateRecipeViewModel.Pages.StepsPage,
    listState: LazyListState = rememberLazyListState(),
    onDeleteItemClick: (Int) -> Unit,
    onStepTextFieldValueChange: (String) -> Unit,
    onAddItemIconClick: () -> Unit,
    onNextButtonClick: () -> Unit
) {
    val localOrientation = LocalConfiguration.current.orientation

    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Tell how to make it",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            state = listState
        ) {
            itemsIndexed(
                items = uiState.recipeSteps
            ) { index, item ->
                val visibleState = remember {
                    MutableTransitionState(false).apply {
                        targetState = true
                    }
                }

                AnimatedVisibility(
                    visibleState = visibleState,
                    enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
                    exit = scaleOut()
                ) {
                    RecipeStepItem(
                        modifier = Modifier
                            .animateItemPlacement(),
                        index = index,
                        item = item.step,
                        onDeleteItemClick = {
                            coroutineScope.launch {
                                visibleState.targetState = false
                                delay(500.milliseconds)
                                onDeleteItemClick(it)
                            }
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(if (localOrientation == Configuration.ORIENTATION_LANDSCAPE) 16.dp else 32.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, MaterialTheme.colorScheme.primaryContainer, shape = CircleShape),
            ) {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = uiState.typeField,
                    onValueChange = onStepTextFieldValueChange,
                    decorationBox = {
                        if (uiState.typeField.isEmpty()) {
                            Text(text = "Write here the steps")
                        }
                        it()
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { onAddItemIconClick() }
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            IconButton(onClick = {
                onAddItemIconClick()
            }) {
                Icon(
                    Icons.Outlined.AddCircle,
                    contentDescription = "Add recipe step",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
        Spacer(modifier = Modifier.height(if (localOrientation == Configuration.ORIENTATION_LANDSCAPE) 8.dp else 64.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = onNextButtonClick) {
                Text(text = "Next")
            }
        }
    }
}

@Preview
@Composable
fun StepsPagePreview() {
    StepsPage(
        CreateRecipeViewModel.Pages.StepsPage(
            recipeSteps = listOf(RecipeStep(0, "first"))
        ),
        onNextButtonClick = {},
        onAddItemIconClick = {},
        onStepTextFieldValueChange = {},
        onDeleteItemClick = {}
    )
}