package hu.tb.minichefy.domain.use_case

import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement

class CalculateMeasurements {

    private val liquidRange = 1..2
    private val massRange = 3..5

    fun simpleProductCalculations(
        productBase: CalculationFood,
        productChanger: CalculationFood
    ): CalculationFood {
        val isLiquid =
            productBase.unitOfMeasurement.ordinal in liquidRange && productChanger.unitOfMeasurement.ordinal in liquidRange
        val isMass =
            productBase.unitOfMeasurement.ordinal in massRange && productChanger.unitOfMeasurement.ordinal in massRange
        val isPiece =
            productBase.unitOfMeasurement.ordinal == 0 && productChanger.unitOfMeasurement.ordinal == 0

        if (!isLiquid && !isMass && !isPiece) throw IllegalArgumentException("Not compatible unit of measurements")

        val result = performCalculation(
            productBase.convertToSmallestUnit(),
            productChanger.convertToSmallestUnit()
        )

        return when {
            isLiquid && result >= 10 -> CalculationFood(
                quantity = result / 10,
                unitOfMeasurement = UnitOfMeasurement.L
            )

            isLiquid -> CalculationFood(quantity = result, unitOfMeasurement = UnitOfMeasurement.DL)
            result >= 1000 -> CalculationFood(
                quantity = result / 1000,
                unitOfMeasurement = UnitOfMeasurement.KG
            )

            result >= 10 -> CalculationFood(
                quantity = result / 10,
                unitOfMeasurement = UnitOfMeasurement.DKG
            )

            isPiece -> CalculationFood(quantity = result, unitOfMeasurement = UnitOfMeasurement.PIECE)
            else -> CalculationFood(quantity = result, unitOfMeasurement = UnitOfMeasurement.G)
        }
    }

    private fun performCalculation(
        numberOne: Float,
        numberTwo: Float,
    ): Float = if ((numberOne + numberTwo) < 0f) 0f else numberOne + numberTwo
}

data class CalculationFood(
    val quantity: Float,
    val unitOfMeasurement: UnitOfMeasurement
) {
    fun convertToSmallestUnit() =
        when (unitOfMeasurement) {
            UnitOfMeasurement.KG -> quantity * 1000
            UnitOfMeasurement.DKG -> quantity * 10
            UnitOfMeasurement.G -> quantity
            UnitOfMeasurement.L -> quantity * 10
            UnitOfMeasurement.DL -> quantity
            UnitOfMeasurement.PIECE -> quantity
        }
}