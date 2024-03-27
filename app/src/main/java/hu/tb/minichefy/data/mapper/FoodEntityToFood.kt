package hu.tb.minichefy.data.mapper

import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.entity.FoodWithTags

class FoodEntityToFood {

    fun map(from: FoodWithTags): Food = from.run {
        Food(
            id = foodEntity.foodId,
            icon = foodEntity.icon,
            title = foodEntity.title,
            quantity = foodEntity.quantity,
            unitOfMeasurement = foodEntity.unitOfMeasurement,
            foodTagList = tags.map { TagEntityToTag().map(it) }
        )
    }
}