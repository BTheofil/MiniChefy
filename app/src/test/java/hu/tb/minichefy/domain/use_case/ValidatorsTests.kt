package hu.tb.minichefy.domain.use_case

import org.junit.Assert.assertEquals
import org.junit.Test

class ValidatorsTests {

    private val countIntegerUseCase = ValidateCountInteger()
    private val quantityUseCase = ValidateQuantity()
    private val numberUseCase = ValidateNumberKeyboard()
    private val textFieldUseCase = ValidateTextField()

    @Test
    fun `Check Count Integer validator`() {
        assertEquals(ValidationResult.SUCCESS, countIntegerUseCase(2))
        assertEquals(ValidationResult.SUCCESS, countIntegerUseCase(1))
        assertEquals(ValidationResult.ERROR, countIntegerUseCase(0))
        assertEquals(ValidationResult.ERROR, countIntegerUseCase(-1))
    }

    @Test
    fun `Check Quantity validator`() {
        assertEquals(ValidationResult.SUCCESS, quantityUseCase(2f))
        assertEquals(ValidationResult.SUCCESS, quantityUseCase(1.1f))
        assertEquals(ValidationResult.ERROR, quantityUseCase(-0.1f))
        assertEquals(ValidationResult.ERROR, quantityUseCase(0f))
    }

    @Test
    fun `Check Number keyboard`() {
        assertEquals(ValidationResult.ERROR, numberUseCase("-5"))
        assertEquals(ValidationResult.ERROR, numberUseCase("- 5"))
        assertEquals(ValidationResult.ERROR, numberUseCase("4.1.2"))
        assertEquals(ValidationResult.ERROR, numberUseCase(".1"))
        assertEquals(ValidationResult.SUCCESS, numberUseCase("5"))
        assertEquals(ValidationResult.SUCCESS, numberUseCase("3.2"))
    }

    @Test
    fun `Check Text field`() {
        assertEquals(ValidationResult.ERROR, textFieldUseCase(" "))
        assertEquals(ValidationResult.ERROR, textFieldUseCase(null))
        assertEquals(ValidationResult.SUCCESS, textFieldUseCase("hello"))
    }
}