package hu.tb.minichefy.presentation.screens.settings.settings_options

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.tb.minichefy.domain.model.storage.FoodTag

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TagScreen(
    foodTagList: List<FoodTag>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "back icon"
                    )
                },
                title = {
                    Text(text = "Edit tags")
                })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            FlowColumn {
                foodTagList.forEach { foodTag ->
                    InputChip(
                        selected = true,
                        onClick = { /*TODO*/ },
                        label = {
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
                        colors = InputChipDefaults.inputChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TagScreenPreview() {
    TagScreen(
        foodTagList = listOf(
            FoodTag(0, "first"),
            FoodTag(1, "second"),
            FoodTag(2, "third"),
        )
    )
}