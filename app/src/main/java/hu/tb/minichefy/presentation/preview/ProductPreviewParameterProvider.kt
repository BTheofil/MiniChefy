package hu.tb.minichefy.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.presentation.screens.components.icons.IconManager

class ProductPreviewParameterProvider : PreviewParameterProvider<List<Food>> {

    override val values: Sequence<List<Food>> = sequenceOf(
        MockProductDomain.mockFoodLists
    )
}

private object MockProductDomain {

    val mockFoodLists = listOf(
        Food(
            0,
            icon = IconManager().getRandomProduct().resource,
            "Apple",
            3f,
            unitOfMeasurement = UnitOfMeasurement.PIECE,
            listOf(
                FoodTag(0, "fruit")
            )
        ),
        Food(
            1,
            icon = IconManager().getRandomProduct().resource,
            "Milk",
            1f,
            unitOfMeasurement = UnitOfMeasurement.L,
            listOf(
                FoodTag(0, "milk"),
                FoodTag(1, "liquid")
            )
        ),
        Food(
            2,
            icon = IconManager().getRandomProduct().resource,
            "Carrot",
            2f,
            unitOfMeasurement = UnitOfMeasurement.KG,
            listOf(
                FoodTag(0, "solid"),
                FoodTag(1, "vegetable")
            )
        )
    )
}