package hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.ingredient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import hu.tb.minichefy.domain.model.recipe.RecipeIngredient
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS
import hu.tb.minichefy.presentation.ui.theme.SMALL_SPACE_BETWEEN_ELEMENTS

@Composable
fun CreateNewIngredientDialog(
    ingredient: RecipeIngredient? = null,
    onDismissRequest: () -> Unit,
    onCancelClick: () -> Unit,
    onProceedClick: (RecipeIngredient) -> Unit
) {
    var ingredientName by remember { mutableStateOf(ingredient?.title ?: "") }
    var amount by remember { mutableStateOf(ingredient?.quantity?.toString() ?: "") }
    var unitOfMeasurement by remember { mutableStateOf(UnitOfMeasurement.PIECE) }
    var isDropdownMenuVisible by remember { mutableStateOf(false) }
    var unitOfMeasurementTextileWidth by remember { mutableIntStateOf(0) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if(ingredient == null) "Add new ingredient" else "Edit ingredient",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                OutlinedTextField(
                    value = ingredientName,
                    onValueChange = { ingredientName = it },
                    enabled = ingredient == null,
                    label = {
                        Text(
                            text = "Ingredient name",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    })
                Spacer(modifier = Modifier.height(SMALL_SPACE_BETWEEN_ELEMENTS))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f),
                        value = amount,
                        onValueChange = { amount = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = {
                            Text(
                                text = "Amount",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                    Spacer(modifier = Modifier.width(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .onGloballyPositioned { coordinates ->
                                    unitOfMeasurementTextileWidth = coordinates.size.width
                                },
                            value = unitOfMeasurement.name,
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text(
                                    text = "Measurement",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { isDropdownMenuVisible = true }) {
                                    Icon(
                                        modifier = Modifier,
                                        imageVector = Icons.Rounded.MoreVert,
                                        contentDescription = "measurements menu icon"
                                    )
                                }
                            },
                        )
                        DropdownMenu(
                            modifier = Modifier
                                .width(with(LocalDensity.current) { unitOfMeasurementTextileWidth.toDp() }),
                            expanded = isDropdownMenuVisible,
                            onDismissRequest = { isDropdownMenuVisible = false }) {
                            UnitOfMeasurement.entries.forEach {
                                DropdownMenuItem(
                                    text = { Text(text = it.name) },
                                    onClick = {
                                        unitOfMeasurement = it
                                        isDropdownMenuVisible = false
                                    })
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCancelClick) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                    TextButton(onClick = {
                        onProceedClick(
                            RecipeIngredient(
                                id = ingredient?.id,
                                title = ingredientName,
                                quantity = amount.toFloat(),
                                unitOfMeasurement = unitOfMeasurement
                            )
                        )
                    }) {
                        Text(text = "Proceed")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CreateNewIngredientDialogPreview() {
    Column {
        CreateNewIngredientDialog(
            onDismissRequest = {},
            onCancelClick = {},
            onProceedClick = {}
        )
    }
}