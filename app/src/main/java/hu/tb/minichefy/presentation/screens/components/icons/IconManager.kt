package hu.tb.minichefy.presentation.screens.components.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import kotlin.random.Random

class IconManager {

    val allSystemMealIconLists = listOf(
        MealIcon.FRIED_EGG,
        MealIcon.JUNK_FOOD,
        MealIcon.HOT_SOUP,
        MealIcon.PIZZA,
        MealIcon.STEAK,
        MealIcon.SWEETS,
    )

    val allProductIconList = listOf(
        ProductIcon.APPLE,
        ProductIcon.AVOCADO,
        ProductIcon.BREAD,
        ProductIcon.CHEESE
    )

    fun getRandomProduct(): ProductIcon{
        return  allProductIconList[Random.nextInt(0, allProductIconList.size)]
    }
}

@Composable
fun iconVectorResource(
    iconResource: Int
): ImageVector = ImageVector.vectorResource(iconResource)
