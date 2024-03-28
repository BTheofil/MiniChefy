package hu.tb.minichefy.presentation.screens.recipe.recipe_create.components

import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement

@Composable
fun <T> TextFieldWithDropdownMenu(
    modifier: Modifier = Modifier,
    textFieldValue: String,
    labelFieldText: String,
    menuItemList: List<T>,
    onMenuItemClick: (T) -> Unit,
) {
    var textFieldSizeWidth by remember { mutableIntStateOf(0) }
    var isDropdownMenuVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                textFieldSizeWidth = coordinates.size.width
            },
        value = textFieldValue,
        onValueChange = {},
        readOnly = true,
        label = {
            Text(
                text = labelFieldText,
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
            .width(with(LocalDensity.current) { textFieldSizeWidth.toDp() }),
        expanded = isDropdownMenuVisible,
        onDismissRequest = { isDropdownMenuVisible = false }) {
        menuItemList.forEach {
            DropdownMenuItem(
                text = { Text(text = it.toString()) },
                onClick = {
                    onMenuItemClick(it)
                    isDropdownMenuVisible = false
                })
        }
    }
}

@Preview
@Composable
private fun TextFieldWithDropdownMenuPreview() {

    var selected by remember {
        mutableStateOf(UnitOfMeasurement.PIECE)
    }

    TextFieldWithDropdownMenu(
        textFieldValue = selected.name,
        labelFieldText = "Mesurement",
        menuItemList = UnitOfMeasurement.entries,
        onMenuItemClick = { selected = it }
    )
}