package hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.presentation.screens.components.icons.MealIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconSelectorSheet(
    sheetState: SheetState,
    mealIconList: List<MealIcon>,
    selectedMealIcon: MealIcon,
    onItemClick: (mealIcon: MealIcon) -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        sheetState = sheetState,
        onDismissRequest = { onDismissRequest() }
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = mealIconList
            ) { foodIcon ->
                ElevatedButton(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    onClick = { onItemClick(foodIcon) },
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 3.dp
                    ),
                    border = if (foodIcon == selectedMealIcon) BorderStroke(
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun IconSelectorSheetPreview() {
    IconSelectorSheet(
        sheetState = rememberModalBottomSheetState(),
        mealIconList = emptyList(),
        selectedMealIcon = MealIcon.JUNK_FOOD,
        onItemClick = {},
        onDismissRequest = {})
}