package hu.tb.minichefy.presentation.util.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import kotlin.random.Random

class IconManager {

    fun getRandomFood(): FoodIcon = FoodIcon.entries[Random.nextInt(0, FoodIcon.entries.size)]

    fun findFoodIconByInt(iconInt: Int): IconResource? =
        FoodIcon.entries.find { it.resource == iconInt }
}

@Composable
fun iconVectorResource(
    iconResource: Int
): ImageVector = ImageVector.vectorResource(iconResource)
