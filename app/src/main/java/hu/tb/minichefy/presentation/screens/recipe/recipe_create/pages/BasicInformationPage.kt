package hu.tb.minichefy.presentation.screens.recipe.recipe_create.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.R
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.AddRemoveRow
import hu.tb.minichefy.presentation.ui.components.bottomBorder
import hu.tb.minichefy.presentation.ui.components.clickableWithoutRipple
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicInformationPage(
    recipeName: String,
    counterDisplayContent: Int = 0,
    onTitleValueChange: (String) -> Unit,
    onRemoveQuantityClick: () -> Unit,
    onAddQuantityClick: () -> Unit,
    onNextPageClick: () -> Unit,
    isQuantityHasError: Boolean
) {
    val focusManager = LocalFocusManager.current

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showIconPicker by remember {
        mutableStateOf(false)
    }

    val list = remember {
        listOf(
            R.drawable.fried_egg_icon,
            R.drawable.pizza_icon,
            R.drawable.hotsoup_icon,
            R.drawable.junk_food_icon,
            R.drawable.steak_icon,
            R.drawable.sweets_icon
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .padding(horizontal = 22.dp)
            .padding(top = 22.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(16.dp)
                    .clickableWithoutRipple {
                        scope.launch {
                            showIconPicker = true
                        }
                    },
                imageVector = ImageVector.vectorResource(R.drawable.fried_egg_icon),
                contentDescription = "Default food icon"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recipe title: ",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .bottomBorder(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 1.dp
                    ),
                value = recipeName,
                onValueChange = onTitleValueChange,
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "How many times can eat?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        AddRemoveRow(
            onAddButtonClick = {
                focusManager.clearFocus()
                onAddQuantityClick()
            },
            onRemoveButtonClick = {
                focusManager.clearFocus()
                onRemoveQuantityClick()
            },
            displayContent = counterDisplayContent,
            isErrorHappened = isQuantityHasError
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            OutlinedButton(
                modifier = Modifier,
                onClick = {
                    focusManager.clearFocus()
                    onNextPageClick()
                }
            ) {
                Text(
                    text = "Next page",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        if (showIconPicker) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxWidth(),
                sheetState = sheetState,
                onDismissRequest = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showIconPicker = false
                        }
                    }
                }
            ) {
                LazyColumn() {
                    items(
                        items = list
                    ) {
                        ElevatedCard {
                            Image(
                                imageVector = ImageVector.vectorResource(it),
                                contentDescription = "recipe icon"
                            )
                        }
                    }
                }
                Row {
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "Dismiss")
                    }
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "Select")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BasicInformationPagePreview() {
    BasicInformationPage("test name", 0, {}, {}, {}, {}, false)
}