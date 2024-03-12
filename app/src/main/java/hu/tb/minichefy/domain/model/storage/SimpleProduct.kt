package hu.tb.minichefy.domain.model.storage

import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement.*

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
}
