package hu.tb.minichefy.data.mapper

import hu.tb.minichefy.domain.model.storage.FoodSummary
import hu.tb.minichefy.domain.model.storage.entity.SimpleFoodEntity

class SimpleFoodEntityToFoodSummery {

    fun map(simpleFoodEntity: SimpleFoodEntity) = simpleFoodEntity.run {
        FoodSummary(
            id = foodId,
            title = title,
            unitOfMeasurement = unitOfMeasurement
        )
    }
}