package hu.tb.minichefy.domain.use_case

import org.junit.Assert.assertEquals
import org.junit.Test

class ValidateCountIntegerTest {

    @Test
    fun `Check Quantity validator works currently`() {
        assertEquals(ValidationResult.SUCCESS, ValidateCountInteger().invoke(2))
        assertEquals(ValidationResult.SUCCESS, ValidateCountInteger().invoke(1))
        assertEquals(ValidationResult.ERROR, ValidateCountInteger().invoke(0))
        assertEquals(ValidationResult.ERROR, ValidateCountInteger().invoke(-1))
    }
}