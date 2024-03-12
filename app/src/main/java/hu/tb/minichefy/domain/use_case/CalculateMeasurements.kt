package hu.tb.minichefy.domain.use_case

import hu.tb.minichefy.domain.model.storage.SimpleProduct
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement

class CalculateMeasurements {

    private val liquidRange = 1..2
    private val massRange = 3..5

    fun simpleProductCalculations(
        productBase: SimpleProduct,
        productChanger: SimpleProduct
    ): SimpleProduct {
        val isLiquid =
            productBase.unitOfMeasurement.id in liquidRange && productChanger.unitOfMeasurement.id in liquidRange
        val isMass =
            productBase.unitOfMeasurement.id in massRange && productChanger.unitOfMeasurement.id in massRange
        val isPiece =
            productBase.unitOfMeasurement.id == 0 && productChanger.unitOfMeasurement.id == 0

        if (!isLiquid && !isMass && !isPiece) throw IllegalArgumentException("Not compatible unit of measurements")

        val result = performCalculation(
            productBase.convertToSmallestUnit(),
            productChanger.convertToSmallestUnit()
        )

        return when {
            isLiquid && result >= 10 -> SimpleProduct(
                quantity = result / 10,
                unitOfMeasurement = UnitOfMeasurement.L
            )

            isLiquid -> SimpleProduct(quantity = result, unitOfMeasurement = UnitOfMeasurement.DL)
            result >= 1000 -> SimpleProduct(
                quantity = result / 1000,
                unitOfMeasurement = UnitOfMeasurement.KG
            )

            result >= 10 -> SimpleProduct(
                quantity = result / 10,
                unitOfMeasurement = UnitOfMeasurement.DKG
            )

            isPiece -> SimpleProduct(quantity = result, unitOfMeasurement = UnitOfMeasurement.PIECE)
            else -> SimpleProduct(quantity = result, unitOfMeasurement = UnitOfMeasurement.G)
        }
    }

    private fun performCalculation(
        numberOne: Float,
        numberTwo: Float,
    ): Float = if ((numberOne + numberTwo) < 0) 0f else numberOne + numberTwo
}