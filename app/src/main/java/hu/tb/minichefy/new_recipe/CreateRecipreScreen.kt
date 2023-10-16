package hu.tb.minichefy.new_recipe

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateRecipe(
    viewModel: CreateRecipeViewModel = viewModel()
) {
    val uiState by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.padding(24.dp)
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
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(
                items = uiState.testList,
            ) { index, item ->
                ElevatedCard {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = (index + 1).toString() + ". ")
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Icon(
                            imageVector = Icons.Outlined.Clear,
                            contentDescription = "Delete step",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.primaryContainer, shape = CircleShape),
        ) {
            BasicTextField(
                modifier = Modifier.padding(16.dp),
                value = uiState.activeField,
                onValueChange = {
                    viewModel.onFieldChange(it)
                },
                decorationBox = {
                    if (uiState.activeField.isEmpty()) {
                        Text(text = "Placeholder")
                    }
                    it()
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.addRecipeStep(uiState.activeField)
                    }
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { viewModel.addRecipeStep(uiState.activeField) }) {
            Text(text = "Add recipe step")
        }
    }

}

@Preview
@Composable
fun CreateRecipePreview() {
    CreateRecipe()
}