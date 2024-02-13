package hu.tb.minichefy.domain.model.storage

import hu.tb.minichefy.domain.model.storage.entity.FoodTagEntity

data class FoodTag(
    val id: Long? = null,
    val tag: String
)

fun FoodTag.toFoodTagEntity() =
    FoodTagEntity(
        id = id,
        tag = tag
    )
