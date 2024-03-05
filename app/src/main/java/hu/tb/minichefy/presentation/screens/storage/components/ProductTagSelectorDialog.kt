package hu.tb.minichefy.presentation.screens.storage.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.presentation.screens.storage.storage_create.StorageCreateViewModel
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS
import hu.tb.minichefy.presentation.ui.theme.SMALL_SPACE_BETWEEN_ELEMENTS

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductTagSelectorDialog(
    dismissAndCloseAction: () -> Unit,
    onTagClick: (tag: FoodTag) -> Unit,
    allTagList: List<FoodTag>,
    selectedTagList: List<FoodTag>
) {
    Dialog(onDismissRequest = dismissAndCloseAction) {
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
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(SMALL_SPACE_BETWEEN_ELEMENTS)
                ) {
                    allTagList.forEach { tagItem ->
                        FilterChip(
                            selected = selectedTagList.contains(tagItem),
                            onClick = { onTagClick(tagItem) },
                            label = {
                                Text(
                                    text = tagItem.tag,
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            leadingIcon = if (selectedTagList.contains(tagItem)) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "Done icon",
                                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                                    )
                                }
                            } else {
                                null
                            },
                        )
                    }
                }
                Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(onClick = dismissAndCloseAction) {
                        Text(text = "Done")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProductTagSelectorDialogPreview() {

    val allTestList = listOf(FoodTag(0, "fruit"), FoodTag(1, "vegetable"))
    val selectedList = mutableListOf<FoodTag>()

    ProductTagSelectorDialog(
        dismissAndCloseAction = { /*TODO*/ },
        allTagList = allTestList,
        selectedTagList = selectedList,
        onTagClick = {
            selectedList.add(it)
        }
    )
}