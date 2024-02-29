package hu.tb.minichefy.domain.use_case

import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import org.junit.Assert
import org.junit.Test

class CalculateMeasurementsTest {

    private val calculateMeasurements = CalculateMeasurements()

    @Test
    fun `Different add`() {
        val result = calculateMeasurements.solidFoodCalculation(
            1,
            UnitOfMeasurement.KG,
            2,
            UnitOfMeasurement.DKG,
            CalculateMeasurements.Calculation.SUBTRACTION
        )
        Assert.assertEquals(CalculateMeasurements.FoodResult(8, UnitOfMeasurement.DKG), result)
    }

    @Test
    fun `Same unit`() {
        val result = calculateMeasurements.solidFoodCalculation(
            2,
            UnitOfMeasurement.KG,
            1,
            UnitOfMeasurement.KG,
            CalculateMeasurements.Calculation.SUBTRACTION
        )
        Assert.assertEquals(CalculateMeasurements.FoodResult(1, UnitOfMeasurement.KG), result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Not compatible unit`() {
        calculateMeasurements.solidFoodCalculation(
                2,
                UnitOfMeasurement.KG,
                1,
                UnitOfMeasurement.L,
                CalculateMeasurements.Calculation.SUBTRACTION
            )
    }
}