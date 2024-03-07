package hu.tb.minichefy.domain.model.storage

import hu.tb.minichefy.domain.model.storage.entity.FoodEntity
import hu.tb.minichefy.domain.model.storage.entity.FoodTagEntity
import hu.tb.minichefy.domain.model.storage.entity.FoodTagListWrapper

data class Food(
    val id: Long? = null,
    val icon: Int,
    val title: String,
    val quantity: Float,
    val unitOfMeasurement: UnitOfMeasurement,
    val foodTagList: List<FoodTag>?
) {
    fun toFoodEntity(): FoodEntity =
        FoodEntity(id = id,
            icon = icon,
            title = title,
            quantity = quantity,
            unitOfMeasurement = unitOfMeasurement,
            tagList = foodTagList?.let { foodTags ->
                FoodTagListWrapper(foodTagList = foodTags.map {
                    FoodTagEntity(it.id, it.tag)
                })
            })
}

enum class UnitOfMeasurement(val id: Int) {
    PIECE(0),
    DL(1),
    L(2),
    G(3),
    DKG(4),
    KG(5)
}
