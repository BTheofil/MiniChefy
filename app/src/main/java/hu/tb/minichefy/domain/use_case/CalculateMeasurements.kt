package hu.tb.minichefy.domain.use_case

import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement

class CalculateMeasurements {

    data class FoodResult(val quantity: Int, val unit: UnitOfMeasurement)

    fun solidFoodCalculation(
        numberOne: Int,
        numberOneUnit: UnitOfMeasurement,
        numberTwo: Int,
        numberTwoUnit: UnitOfMeasurement,
        calculation: Calculation
    ): FoodResult {
        return if (numberOneUnit == numberTwoUnit) {
            val quantity = performCalculation(numberOne, numberTwo, calculation)
            FoodResult(quantity, numberOneUnit)
        } else {

            if (numberOneUnit.id in 1..2 && numberTwoUnit.id in 1..2) {
                val convertedNumberOne = convertToDeciliter(numberOne, numberOneUnit)
                val convertedNumberTwo = convertToDeciliter(numberTwo, numberTwoUnit)

                val result = performCalculation(convertedNumberOne, convertedNumberTwo, calculation)
                when {
                    result >= 10 -> {
                        FoodResult(result / 10, UnitOfMeasurement.L)
                    }

                    else -> {
                        FoodResult(result, UnitOfMeasurement.DL)
                    }
                }
            } else if (numberOneUnit.id in 3..5 && numberTwoUnit.id in 3..5) {
                val convertedNumberOne = convertToGrams(numberOne, numberOneUnit)
                val convertedNumberTwo = convertToGrams(numberTwo, numberTwoUnit)

                val result = performCalculation(convertedNumberOne, convertedNumberTwo, calculation)
                when {
                    result >= 1000 -> {
                        FoodResult(result / 1000, UnitOfMeasurement.KG)
                    }

                    result >= 100 -> {
                        FoodResult(result / 100, UnitOfMeasurement.DKG)
                    }

                    else -> {
                        FoodResult(result, UnitOfMeasurement.G)
                    }
                }
            } else {
                throw IllegalArgumentException("Not the same type")
            }
        }
    }

    private fun performCalculation(numberOne: Int, numberTwo: Int, calculation: Calculation): Int {
        return if (calculation == Calculation.ADD) {
            numberOne + numberTwo
        } else {
            if (numberOne - numberTwo <= 0) {
                0
            } else {
                numberOne - numberTwo
            }
        }
    }

    private fun convertToGrams(quantity: Int, unit: UnitOfMeasurement): Int {
        return when (unit) {
            UnitOfMeasurement.KG -> quantity * 1000
            UnitOfMeasurement.DKG -> quantity * 100
            UnitOfMeasurement.G -> quantity
            else -> throw IllegalArgumentException("Unsupported unit: $unit")
        }
    }

    private fun convertToDeciliter(quantity: Int, unit: UnitOfMeasurement): Int {
        return when (unit) {
            UnitOfMeasurement.L -> quantity * 10
            UnitOfMeasurement.DL -> quantity
            else -> throw IllegalArgumentException("Unsupported unit: $unit")
        }
    }

    enum class Calculation {
        ADD, SUBTRACTION
    }
}