package hu.tb.minichefy.presentation.screens.storage.storage_create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.presentation.screens.components.CircleImage
import hu.tb.minichefy.presentation.screens.components.QuestionRowAnswer
import hu.tb.minichefy.presentation.screens.storage.storage_create.components.RadioButtonWithText
import hu.tb.minichefy.presentation.ui.components.bottomBorder
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SCREEN_VERTICAL_PADDING
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StorageCreateContent(
    uiState: StorageCreateViewModel.UiState,
    onEvent: (StorageCreateViewModel.OnEvent) -> Unit
) {
    var isUnitMenuExpanded by remember { mutableStateOf(false) }

    var isTagPopupVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = SCREEN_HORIZONTAL_PADDING, vertical = SCREEN_VERTICAL_PADDING)
    ) {
        CircleImage(
            image = uiState.productIcon.resource
        )

        QuestionRowAnswer(
            questionText = "Title:",
            textFieldValue = uiState.productTitleText,
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
                            isSelected = uiState.productType == it,
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
            Spacer(modifier = Modifier.width(MEDIUM_SPACE_BETWEEN_ELEMENTS))
            Box(
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .bottomBorder(
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 1.dp
                            ),
                        text = if (uiState.productUnitOfMeasurement != null) uiState.productUnitOfMeasurement.toString() else "",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(SMALL_SPACE_BETWEEN_ELEMENTS))
                    IconButton(onClick = {
                        isUnitMenuExpanded = true
                    }) {
                        Icon(
                            Icons.Outlined.MoreVert,
                            contentDescription = "Unit od measurement dropdown icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                DropdownMenu(
                    expanded = isUnitMenuExpanded,
                    onDismissRequest = { isUnitMenuExpanded = false },
                ) {
                    repeat(uiState.availableUnitOfMeasurementList.size) { index ->
                        uiState.availableUnitOfMeasurementList[index].also { uom ->
                            DropdownMenuItem(
                                text = { Text(uom.toString()) },
                                onClick = {
                                    onEvent(
                                        StorageCreateViewModel.OnEvent.FoodUnitChange(
                                            uom
                                        )
                                    )
                                    isUnitMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = "Select tag",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(SMALL_SPACE_BETWEEN_ELEMENTS),
            verticalArrangement = Arrangement.spacedBy(SMALL_SPACE_BETWEEN_ELEMENTS)
        ) {
            AssistChip(
                onClick = { isTagPopupVisible = true },
                label = {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "add more tag icon chip"
                    )
                })
            repeat(uiState.tagList.size) {
                AssistChip(
                    onClick = { onEvent(StorageCreateViewModel.OnEvent.RemoveTag(it)) },
                    label = {
                        Text(text = uiState.tagList[it])
                    })
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(onClick = { onEvent(StorageCreateViewModel.OnEvent.Save) }) {
                Text(text = "Save")
            }
        }
    }

    if (isTagPopupVisible) {
        Dialog(onDismissRequest = { isTagPopupVisible = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(vertical = 16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = uiState.tagDialogValue,
                        onValueChange = {
                            onEvent(StorageCreateViewModel.OnEvent.TagValueChange(it))
                        },
                        label = {
                            Text(text = "Write new tag")
                        })
                    Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        OutlinedButton(onClick = { isTagPopupVisible = false }) {
                            Text(text = "Close")
                        }
                        Button(onClick = { onEvent(StorageCreateViewModel.OnEvent.AddTagToList) }) {
                            Text(text = "Add")
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