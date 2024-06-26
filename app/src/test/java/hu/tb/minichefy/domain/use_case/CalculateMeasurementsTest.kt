package hu.tb.minichefy.domain.use_case

import hu.tb.minichefy.domain.exceptions.NotCompatibleCalculation
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import org.junit.Assert
import org.junit.Test

class CalculateMeasurementsTest {

    private val calculateMeasurements = CalculateMeasurements()

    //mass
    @Test
    fun `MASS Different unit of measurement ADDITION`() {
        val testS1 = CalculationFood(1f, UnitOfMeasurement.KG)
        val testS2 = CalculationFood(2f, UnitOfMeasurement.DKG)

        val calculation = calculateMeasurements.simpleProductCalculations(
            testS1, testS2
        )
        Assert.assertEquals(CalculationFood(1.02f, UnitOfMeasurement.KG), calculation)
    }

    @Test
    fun `MASS Different unit of measurement SUBSTRATION`() {
        val testS1 = CalculationFood(1f, UnitOfMeasurement.KG)
        val testS2 = CalculationFood((-2f), UnitOfMeasurement.DKG)

        val calculation = calculateMeasurements.simpleProductCalculations(
            testS1, testS2,
        )
        Assert.assertEquals(CalculationFood(98f, UnitOfMeasurement.DKG), calculation)
    }

    @Test
    fun `MASS Same unit of measurements ADDICTION`() {
        val testS1 = CalculationFood(1f, UnitOfMeasurement.DKG)
        val testS2 = CalculationFood(2f, UnitOfMeasurement.DKG)

        val result = calculateMeasurements.simpleProductCalculations(
            testS1, testS2
        )
        Assert.assertEquals(CalculationFood(3f, UnitOfMeasurement.DKG), result)
    }

    @Test
    fun `MASS Same unit of measurements SUBSTRATION`() {
        val testS1 = CalculationFood(3f, UnitOfMeasurement.KG)
        val testS2 = CalculationFood(-2f, UnitOfMeasurement.KG)

        val result = calculateMeasurements.simpleProductCalculations(
            testS1, testS2
        )
        Assert.assertEquals(CalculationFood(1f, UnitOfMeasurement.KG), result)
    }

    @Test(expected = NotCompatibleCalculation::class)
    fun `MASS Not compatible unit of measurements`() {
        val testS1 = CalculationFood(3f, UnitOfMeasurement.KG)
        val testS2 = CalculationFood(-2f, UnitOfMeasurement.L)

        calculateMeasurements.simpleProductCalculations(
            testS1, testS2
        )
    }

    @Test
    fun `MASS Fractional numbers ADDICTION`() {
        val testS1 = CalculationFood(1.1f, UnitOfMeasurement.KG)
        val testS2 = CalculationFood(2f, UnitOfMeasurement.DKG)

        val result = calculateMeasurements.simpleProductCalculations(
            testS1, testS2
        )
        Assert.assertEquals(CalculationFood(1.12f, UnitOfMeasurement.KG), result)
    }

    @Test
    fun `MASS Fractional numbers SUBSTRATION`() {
        val testS1 = CalculationFood(1.1f, UnitOfMeasurement.KG)
        val testS2 = CalculationFood(-2f, UnitOfMeasurement.DKG)

        val result = calculateMeasurements.simpleProductCalculations(
            testS1, testS2,
        )
        Assert.assertEquals(CalculationFood(1.08f, UnitOfMeasurement.KG), result)
    }

    @Test
    fun `MASS Small numbers ADDICTION`() {
        val testS1 = CalculationFood(1f, UnitOfMeasurement.KG)
        val testS2 = CalculationFood(1f, UnitOfMeasurement.G)

        val result = calculateMeasurements.simpleProductCalculations(
            testS1, testS2,
        )
        Assert.assertEquals(CalculationFood(1.001f, UnitOfMeasurement.KG), result)
    }

    @Test
    fun `MASS Small numbers SUBSTRATION`() {
        val testS1 = CalculationFood(1f, UnitOfMeasurement.KG)
        val testS2 = CalculationFood(-50f, UnitOfMeasurement.G)

        val result = calculateMeasurements.simpleProductCalculations(
            testS1, testS2,
        )
        Assert.assertEquals(CalculationFood(95f, UnitOfMeasurement.DKG), result)
    }

    @Test
    fun `MASS sp2 greater than sp1 SUBSTRATION`() {
        val testS1 = CalculationFood(1f, UnitOfMeasurement.G)
        val testS2 = CalculationFood(-2f, UnitOfMeasurement.G)

        val calculation = calculateMeasurements.simpleProductCalculations(
            testS1, testS2,
        )
        Assert.assertEquals(CalculationFood(0f, UnitOfMeasurement.G), calculation)
    }

    //liquid
    @Test
    fun `LIQUID Different unit of measurement ADDITION`() {
        val testS1 = CalculationFood(1f, UnitOfMeasurement.L)
        val testS2 = CalculationFood(2f, UnitOfMeasurement.DL)

        val calculation = calculateMeasurements.simpleProductCalculations(
            testS1, testS2
        )
        Assert.assertEquals(CalculationFood(1.2f, UnitOfMeasurement.L), calculation)
    }

    @Test
    fun `LIQUID Different unit of measurement SUBSTRATION`() {
        val testS1 = CalculationFood(1f, UnitOfMeasurement.L)
        val testS2 = CalculationFood(-2f, UnitOfMeasurement.DL)

        val calculation = calculateMeasurements.simpleProductCalculations(
            testS1, testS2
        )
        Assert.assertEquals(CalculationFood(8f, UnitOfMeasurement.DL), calculation)
    }

    @Test
    fun `LIQUID Same unit of measurements ADDICTION`() {
        val testS1 = CalculationFood(1f, UnitOfMeasurement.DL)
        val testS2 = CalculationFood(2f, UnitOfMeasurement.DL)

        val calculation = calculateMeasurements.simpleProductCalculations(
            testS1, testS2
        )
        Assert.assertEquals(CalculationFood(3f, UnitOfMeasurement.DL), calculation)
    }

    @Test
    fun `LIQUID Same unit of measurements SUBSTRATION`() {
        val testS1 = CalculationFood(3f, UnitOfMeasurement.DL)
        val testS2 = CalculationFood(-2f, UnitOfMeasurement.DL)

        val calculation = calculateMeasurements.simpleProductCalculations(
            testS1, testS2,
        )
        Assert.assertEquals(CalculationFood(1f, UnitOfMeasurement.DL), calculation)
    }

    @Test
    fun `LIQUID sp2 greater than sp1 SUBSTRATION`() {
        val testS1 = CalculationFood(1f, UnitOfMeasurement.DL)
        val testS2 = CalculationFood(-2f, UnitOfMeasurement.DL)

        val calculation = calculateMeasurements.simpleProductCalculations(
            testS1, testS2
        )
        Assert.assertEquals(CalculationFood(0f, UnitOfMeasurement.DL), calculation)
    }
}