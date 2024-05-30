package hu.tb.minichefy.presentation.util.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import hu.tb.minichefy.domain.model.IconResource
import kotlin.random.Random

class IconManager {

    fun getRandomFood(): FoodIcon {
        val size = FoodIcon.entries.size
        return FoodIcon.entries[Random.nextInt(0, size)]
    }

    fun findFoodIconByInt(iconInt: Int): IconResource? =
        FoodIcon.entries.find { it.resource == iconInt }
}

@Composable
fun iconVectorResource(
    iconResource: Int
): ImageVector = ImageVector.vectorResource(iconResource)
