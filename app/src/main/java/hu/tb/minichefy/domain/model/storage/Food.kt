package hu.tb.minichefy.domain.model.storage

import hu.tb.minichefy.domain.model.storage.entity.FoodEntity

data class Food(
    val id: Long? = null,
    val title: String,
    val quantity: Int,
    val unitOfMeasurement: UnitOfMeasurement,
    val type: String
)

fun Food.toFoodEntity() = FoodEntity(
    id = this.id,
    title = title,
    quantity = quantity,
    unitOfMeasurement = unitOfMeasurement,
    type = type
)

enum class UnitOfMeasurement(val id: Int) {
    DL(1),
    L(2),
    G(3),
    DKG(4),
    KG(5),
}
