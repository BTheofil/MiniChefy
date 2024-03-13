package hu.tb.minichefy.domain.model.storage

import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement.*
import hu.tb.minichefy.presentation.screens.components.icons.IconManager

data class SimpleProduct(
    val title: String? = null,
    val quantity: Float,
    val unitOfMeasurement: UnitOfMeasurement
) {
    fun convertToSmallestUnit() =
        when (unitOfMeasurement) {
            KG -> quantity * 1000
            DKG -> quantity * 10
            G -> quantity
            L -> quantity * 10
            DL -> quantity
            PIECE -> quantity
        }

    fun toProduct() =
        Food(
            id = null,
            title = title!!,
            icon = IconManager().getRandomProduct().resource,
            quantity = quantity,
            unitOfMeasurement = unitOfMeasurement,
            foodTagList = emptyList()
        )
}
