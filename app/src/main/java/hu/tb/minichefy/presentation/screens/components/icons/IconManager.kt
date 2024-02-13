package hu.tb.minichefy.presentation.screens.components.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

class IconManager {

    val foodIcons = listOf(
        FoodIcon.FRIED_EGG,
        FoodIcon.JUNK_FOOD,
        FoodIcon.HOT_SOUP,
        FoodIcon.PIZZA,
        FoodIcon.STEAK,
        FoodIcon.SWEETS,
    )
}

@Composable
fun iconVectorResource(
    iconResource: Int
): ImageVector = ImageVector.vectorResource(iconResource)
