package hu.tb.minichefy.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.presentation.screens.manager.icons.IconManager

class FoodPreviewParameterProvider : PreviewParameterProvider<List<Food>> {

    override val values: Sequence<List<Food>> = sequenceOf(
        MockFoodDomain.mockFoodLists
    )
}

object MockFoodDomain {

    val mockFoodLists = listOf(
        Food(
            id = 0,
            icon = IconManager().getRandomFood().resource,
            title = "Apple",
            quantity = 3f,
            unitOfMeasurement = UnitOfMeasurement.PIECE,
            foodTagList = listOf(
                FoodTag(0, "fruit")
            )
        ),
        Food(
            id = 1,
            icon = IconManager().getRandomFood().resource,
            title = "Milk",
            quantity = 1f,
            unitOfMeasurement = UnitOfMeasurement.L,
            foodTagList = listOf(
                FoodTag(0, "milk"),
                FoodTag(1, "liquid")
            )
        ),
        Food(
            id = 2,
            icon = IconManager().getRandomFood().resource,
            title = "Carrot",
            quantity = 2f,
            unitOfMeasurement = UnitOfMeasurement.KG,
            foodTagList = listOf(
                FoodTag(0, "solid"),
                FoodTag(1, "vegetable")
            )
        )
    )
}