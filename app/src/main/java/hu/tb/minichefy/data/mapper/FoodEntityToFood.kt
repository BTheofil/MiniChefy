package hu.tb.minichefy.data.mapper

import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.entity.FoodEntity
import hu.tb.minichefy.domain.model.storage.entity.toFoodTag

class FoodEntityToFood {

    fun map(from: FoodEntity): Food = from.run {
        Food(
            id = id,
            icon = icon,
            title = title,
            quantity = quantity,
            unitOfMeasurement = unitOfMeasurement,
            foodTagList = tagList?.toFoodTag()
        )
    }
}