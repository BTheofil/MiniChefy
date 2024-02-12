package hu.tb.minichefy.data.mapper

import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.entity.StorageFoodEntity

class FoodEntityToFood {

    fun map(from: StorageFoodEntity): Food = from.run {
        Food(
            id = id,
            title = title,
            quantity = quantity,
            unitOfMeasurement = unitOfMeasurement
        )
    }
}