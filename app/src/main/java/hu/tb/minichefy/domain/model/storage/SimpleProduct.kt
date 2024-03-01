package hu.tb.minichefy.domain.model.storage

data class SimpleProduct(
    val quantity: Float,
    val unitOfMeasurement: UnitOfMeasurement
) {
    fun convertToSmallestUnit() =
        when (this.unitOfMeasurement) {
            UnitOfMeasurement.KG -> quantity * 1000
            UnitOfMeasurement.DKG -> quantity * 10
            UnitOfMeasurement.G -> quantity
            UnitOfMeasurement.L -> quantity * 10
            UnitOfMeasurement.DL -> quantity
        }
}
