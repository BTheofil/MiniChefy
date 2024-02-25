package hu.tb.minichefy.domain.model.storage

import hu.tb.minichefy.domain.model.storage.entity.FoodEntity
import hu.tb.minichefy.domain.model.storage.entity.FoodTagListWrapper

data class Food(
    val id: Long? = null,
    val title: String,
    val quantity: Int,
    val unitOfMeasurement: UnitOfMeasurement,
    val foodTagList: FoodTagListWrapper?
)

fun Food.toFoodEntity() = FoodEntity(
    id = id,
    title = title,
    quantity = quantity,
    unitOfMeasurement = unitOfMeasurement,
    tagList = foodTagList
)

enum class UnitOfMeasurement(val id: Int) {
    DL(1),
    L(2),
    G(3),
    DKG(4),
    KG(5),
}
