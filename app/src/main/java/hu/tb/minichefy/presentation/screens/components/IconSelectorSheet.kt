package hu.tb.minichefy.presentation.screens.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.presentation.screens.manager.icons.IconManager
import hu.tb.minichefy.presentation.screens.manager.icons.IconResource
import hu.tb.minichefy.presentation.screens.manager.icons.MealIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconSelectorSheet(
    allIconList: List<IconResource>,
    selectedIcon: IconResource?,
    onItemClick: (icon: IconResource) -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismissRequest
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = allIconList
            ) { foodIcon ->
                ElevatedButton(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    onClick = { onItemClick(foodIcon) },
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 3.dp
                    ),
                    border = if (foodIcon == selectedIcon) BorderStroke(
                        8.dp,
                        MaterialTheme.colorScheme.primary
                    ) else null
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(foodIcon.resource),
                        contentDescription = "recipe food icon"
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview
@Composable
fun IconSelectorSheetPreview() {
    var isSheetVisible by remember {
        mutableStateOf(false)
    }
    var selectedIcon by remember {
        mutableStateOf<IconResource>(MealIcon.JUNK_FOOD)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        TextButton(onClick = {
            isSheetVisible = true
        }) {
            Text(text = "Show bottom sheet")
        }

        if (isSheetVisible) {
            IconSelectorSheet(
                allIconList = IconManager().getAllSystemMealIconLists,
                selectedIcon = selectedIcon,
                onItemClick = { selectedIcon = it },
                onDismissRequest = { isSheetVisible = false }
            )
        }
    }
}