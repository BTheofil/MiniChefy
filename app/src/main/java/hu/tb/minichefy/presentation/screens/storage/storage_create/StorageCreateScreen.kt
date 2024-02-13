package hu.tb.minichefy.presentation.screens.storage.storage_create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.presentation.screens.components.QuestionRowAnswer
import hu.tb.minichefy.presentation.screens.storage.storage_create.components.RadioButtonWithText
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SMALL_SPACE_BETWEEN_ELEMENTS

@Composable
fun StorageCreateScreen(
    viewModel: StorageCreateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StorageCreateContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun StorageCreateContent(
    uiState: StorageCreateViewModel.UiState,
    onEvent: (StorageCreateViewModel.OnEvent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = SCREEN_HORIZONTAL_PADDING)
    ) {
        QuestionRowAnswer(
            questionText = "Title:",
            textFieldValue = uiState.foodTitleText,
            onTextFieldValueChange = { onEvent(StorageCreateViewModel.OnEvent.FoodTextChange(it)) }
        )
        Spacer(modifier = Modifier.height(SMALL_SPACE_BETWEEN_ELEMENTS))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Type:",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                repeat(StorageCreateViewModel.FoodType.entries.size) { foodTypeIndex ->
                    StorageCreateViewModel.FoodType.entries[foodTypeIndex].also {
                        RadioButtonWithText(
                            displayText = it.displayText,
                            isSelected = uiState.foodType == it,
                            onClick = {
                                onEvent(
                                    StorageCreateViewModel.OnEvent.FoodTypeChange(
                                        it
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(SMALL_SPACE_BETWEEN_ELEMENTS))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Unit of measurement:",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
            )
            Box(
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (uiState.selectedFoodUnitOfMeasurement != null) uiState.selectedFoodUnitOfMeasurement.toString() else "",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.width(SMALL_SPACE_BETWEEN_ELEMENTS))
                    IconButton(onClick = {
                        expanded = true
                    }) {
                        Icon(
                            Icons.Outlined.MoreVert,
                            contentDescription = "Unit od measurement dropdown icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    repeat(uiState.foodUnitOfMeasurement.size) { index ->
                        uiState.foodUnitOfMeasurement[index].also { uom ->
                            DropdownMenuItem(
                                text = { Text(uom.toString()) },
                                onClick = {
                                    onEvent(
                                        StorageCreateViewModel.OnEvent.FoodUnitChange(
                                            uom
                                        )
                                    )
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun StorageCreateContentPreview() {
    StorageCreateContent(StorageCreateViewModel.UiState()) {}
}