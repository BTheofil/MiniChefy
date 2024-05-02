package hu.tb.minichefy.presentation.screens.settings.settings_options

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.presentation.screens.settings.SettingsViewModel
import hu.tb.minichefy.presentation.screens.settings.settings_options.components.AddTagDialog
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SMALL_SPACE_BETWEEN_ELEMENTS

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TagScreen(
    onNavigateBackClick: () -> Unit,
    foodTagList: List<FoodTag>,
    tagState: SettingsViewModel.TagState,
    updateTagTextFieldValue: (String) -> Unit,
    saveNewTag: () -> Unit,
    deleteTag: (id: Long) -> Unit
) {
    var isTagCreateDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigateBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "back icon"
                        )
                    }
                },
                title = {
                    Text(text = "Edit tags")
                })
        }
    ) { paddingValues ->
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = SCREEN_HORIZONTAL_PADDING),
            horizontalArrangement = Arrangement.spacedBy(SMALL_SPACE_BETWEEN_ELEMENTS),
        ) {
            BasicInputChip(
                content = {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "add new tag icon",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                onClick = { isTagCreateDialogVisible = true }
            )
            foodTagList.forEach { foodTag ->
                BasicInputChip(
                    content = {
                        Text(
                            text = foodTag.tag,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "delete icon",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    },
                    onClick = {
                        deleteTag(foodTag.id!!)
                    }
                )
            }
        }
    }

    if (isTagCreateDialogVisible) {
        AddTagDialog(
            onDismissRequest = { isTagCreateDialogVisible = false },
            onConfirmClick = {
                isTagCreateDialogVisible = false
                saveNewTag()
            },
            onCancelClick = { isTagCreateDialogVisible = false },
            textFieldValue = tagState.tagText,
            onValueChange = updateTagTextFieldValue,
            isError = !tagState.isValid
        )
    }
}

@Composable
private fun BasicInputChip(
    content: @Composable () -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    InputChip(
        selected = true,
        onClick = onClick,
        label = content,
        trailingIcon = trailingIcon,
        colors = InputChipDefaults.inputChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        )
    )
}

@Preview
@Composable
private fun TagScreenPreview() {
    TagScreen(
        onNavigateBackClick = {},
        foodTagList = listOf(
            FoodTag(0, "first"),
            FoodTag(1, "second"),
            FoodTag(2, "third"),
            FoodTag(2, "third"),
            FoodTag(2, "third"),
        ),
        tagState = SettingsViewModel.TagState(),
        updateTagTextFieldValue = {},
        saveNewTag = {},
        deleteTag = {}
    )
}