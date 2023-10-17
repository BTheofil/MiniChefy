package hu.tb.minichefy.new_recipe

import android.content.res.Configuration
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.tb.minichefy.new_recipe.components.RecipeStepItem

@Composable
fun CreateRecipe(
    viewModel: CreateRecipeViewModel = viewModel()
) {
    val uiState by viewModel.state.collectAsState()

    val listState = rememberLazyListState()
    LaunchedEffect(key1 = uiState.testList) {
        listState.animateScrollToItem(uiState.testList.lastIndex)
    }

    RecipeStepContent(
        uiState = uiState,
        listState,
        onItemCloseClick = {
            viewModel.removeRecipeStep(it)
        },
        onStepTextFieldValueChange = {
            viewModel.onFieldChange(it)
        },
        onAddItemIconClick = {
            viewModel.addRecipeStep(uiState.activeField)
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeStepContent(
    uiState: CreateRecipeViewModel.UiState,
    listState: LazyListState = rememberLazyListState(),
    onItemCloseClick: (Int) -> Unit,
    onStepTextFieldValueChange: (String) -> Unit,
    onAddItemIconClick: () -> Unit
) {
    val localOrientation = LocalConfiguration.current.orientation

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
                items = uiState.testList,
                key = { _, item -> item.id }
            ) { index, item ->
                RecipeStepItem(
                    modifier = Modifier.animateItemPlacement(),
                    index = index,
                    item = item.step,
                    onItemCloseClick = onItemCloseClick
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
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
                    value = uiState.activeField,
                    onValueChange = onStepTextFieldValueChange,
                    decorationBox = {
                        if (uiState.activeField.isEmpty()) {
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
            IconButton(onClick = onAddItemIconClick) {
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
            Button(onClick = {

            }) {
                Text(text = "Next")
            }
        }
    }
}

@Preview
@Composable
fun CreateRecipePreview() {
    CreateRecipe()
}