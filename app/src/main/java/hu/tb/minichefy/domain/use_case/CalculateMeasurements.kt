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

        if (!isLiquid && !isMass) throw IllegalArgumentException("Not compatible unit of measurements")

        val result = performCalculation(
            productBase.convertToSmallestUnit(),
            productChanger.convertToSmallestUnit()
        )

        return when {
            isLiquid && result >= 10 -> SimpleProduct(result / 10, UnitOfMeasurement.L)
            isLiquid -> SimpleProduct(result, UnitOfMeasurement.DL)
            result >= 1000 -> SimpleProduct(result / 1000, UnitOfMeasurement.KG)
            result >= 10 -> SimpleProduct(result / 10, UnitOfMeasurement.DKG)
            else -> SimpleProduct(result, UnitOfMeasurement.G)
        }
    }

    private fun performCalculation(
        numberOne: Float,
        numberTwo: Float,
    ): Float = if ((numberOne + numberTwo) < 0) 0f else numberOne + numberTwo
}