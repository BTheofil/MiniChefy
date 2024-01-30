package hu.tb.minichefy.presentation.screens.recipe.recipe_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.presentation.screens.recipe.recipe_list.RecipeListViewModel
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPanel(
    modalSheetState: SheetState,
    dismissSettingPanel: () -> DisposableHandle,
    onEvent: (RecipeListViewModel.OnEvent) -> Unit
) {
    ModalBottomSheet(
        sheetState = modalSheetState,
        onDismissRequest = {
            dismissSettingPanel()
        }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .padding(bottom = 22.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            onEvent(RecipeListViewModel.OnEvent.DeleteRecipe)
                            dismissSettingPanel()
                        }
                    ),
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete recipe icon",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(22.dp))
                Text(
                    text = "Delete recipe",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SettingsPanelPreview() {

    var settingPanelVisible by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = {
            settingPanelVisible = true
        }) {
            Text(text = "Show panel")
        }
        if (settingPanelVisible) {
            SettingsPanel(modalSheetState, {
                scope.launch {
                    modalSheetState.hide()
                }.invokeOnCompletion {
                    if (!modalSheetState.isVisible) {
                        settingPanelVisible = false
                    }
                }
            }, {})
        }
    }
}