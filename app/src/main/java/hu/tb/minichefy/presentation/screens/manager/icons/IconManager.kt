package hu.tb.minichefy.presentation.screens.manager.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import kotlin.random.Random

class IconManager {

    val getAllSystemMealIconLists = listOf(
        MealIcon.FRIED_EGG,
        MealIcon.JUNK_FOOD,
        MealIcon.HOT_SOUP,
        MealIcon.PIZZA,
        MealIcon.STEAK,
        MealIcon.SWEETS,
    )

    val getAllProductIconList = listOf(
        ProductIcon.APPLE,
        ProductIcon.AVOCADO,
        ProductIcon.BREAD,
        ProductIcon.CHEESE
    )

    val getDefaultIcon: AppWideIcon = AppWideIcon.DEFAULT_EMPTY_ICON

    fun getRandomProduct(): ProductIcon {
        return  getAllProductIconList[Random.nextInt(0, getAllProductIconList.size)]
    }

    fun convertIntToProductIcon(iconInt: Int): IconResource? = getAllProductIconList.find { it.resource == iconInt}

}

@Composable
fun iconVectorResource(
    iconResource: Int
): ImageVector = ImageVector.vectorResource(iconResource)
