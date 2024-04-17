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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.tb.minichefy.R
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.FoodType
import hu.tb.minichefy.presentation.screens.components.CircleImage
import hu.tb.minichefy.presentation.screens.components.IconSelectorSheet
import hu.tb.minichefy.presentation.screens.components.QuantityAndMeasurementRow
import hu.tb.minichefy.presentation.screens.storage.components.ProductTagSelectorDialog
import hu.tb.minichefy.presentation.screens.storage.storage_create.components.RadioButtonWithText
import hu.tb.minichefy.presentation.screens.components.extensions.clickableWithoutRipple
import hu.tb.minichefy.presentation.screens.manager.icons.FoodIcon
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SCREEN_VERTICAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SMALL_SPACE_BETWEEN_ELEMENTS

@Composable
fun StorageCreateScreen(
    viewModel: StorageCreateViewModel = hiltViewModel(),
    saveSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                StorageCreateViewModel.UiEvent.SaveSuccess -> saveSuccess()
            }
        }
    }

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
    var isTagPopupVisible by remember { mutableStateOf(false) }
    var showIconPicker by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = SCREEN_HORIZONTAL_PADDING, vertical = SCREEN_VERTICAL_PADDING)
            .clickableWithoutRipple { focusManager.clearFocus() }
    ) {

        CircleImage(
            image = uiState.selectedFoodIcon.resource,
            onClick = { showIconPicker = true }
        )
        Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = uiState.foodTitleText,
            onValueChange = { onEvent(StorageCreateViewModel.OnEvent.FoodTextChange(it)) },
            label = {
                Text(
                    text = stringResource(id = R.string.title),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            isError = uiState.isFoodTitleHasError
        )
        Spacer(modifier = Modifier.height(SMALL_SPACE_BETWEEN_ELEMENTS))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.type),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                repeat(FoodType.entries.size) { foodTypeIndex ->
                    FoodType.entries[foodTypeIndex].also {
                        RadioButtonWithText(
                            displayText = stringResource(id = it.stringResource),
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
        QuantityAndMeasurementRow(
            quantityValue = uiState.quantity,
            onQuantityChange = { onEvent(StorageCreateViewModel.OnEvent.FoodQuantityChange(it)) },
            quantityLabel = stringResource(R.string.amount),
            isQuantityHasError = uiState.isQuantityHasError,
            measurementOptionList = uiState.availableUnitOfMeasurementList,
            measurementValue = uiState.foodUnitOfMeasurement,
            measurementLabel = stringResource(R.string.measurement),
            onMeasurementChange = {
                onEvent(StorageCreateViewModel.OnEvent.FoodUnitChange(it))
            }
        )
        Spacer(modifier = Modifier.height(SMALL_SPACE_BETWEEN_ELEMENTS))
        Text(
            text = stringResource(R.string.tag),
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
            uiState.selectedTagList.forEach { tagItem ->
                AssistChip(
                    onClick = { onEvent(StorageCreateViewModel.OnEvent.DialogChipClick(tagItem)) },
                    label = {
                        Text(text = tagItem.tag)
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "remove tag icon"
                        )
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
                Text(text = stringResource(id = R.string.save))
            }
        }
    }

    if (showIconPicker) {
        IconSelectorSheet(
            allIconList = uiState.allFoodIcons,
            selectedIcon = uiState.selectedFoodIcon,
            onItemClick = {
                onEvent(StorageCreateViewModel.OnEvent.OnSelectedIconDialogClick(it as FoodIcon))
            },
            onDismissRequest = { showIconPicker = false }
        )
    }

    if (isTagPopupVisible) {
        ProductTagSelectorDialog(
            dismissAndCloseAction = {
                isTagPopupVisible = false
            },
            onTagClick = {
                onEvent(StorageCreateViewModel.OnEvent.DialogChipClick(it))
            },
            allTagList = uiState.labelFilterTagList,
            selectedTagList = uiState.selectedTagList
        )
    }
}

@Preview
@Composable
fun StorageCreateContentPreview() {
    StorageCreateContent(
        StorageCreateViewModel.UiState(
            labelFilterTagList = listOf(
                FoodTag(0, "fruit"),
                FoodTag(2, "vegetable"),
            )
        )
    ) {}
}