package hu.tb.minichefy.domain.use_case

import org.junit.Assert.assertEquals
import org.junit.Test

class ValidateQuantityNumberTest {

    @Test
    fun `Check Quantity validator works currently`() {
        assertEquals(ValidationResult.SUCCESS, ValidateQuantityNumber().invoke(2))
        assertEquals(ValidationResult.SUCCESS, ValidateQuantityNumber().invoke(1))
        assertEquals(ValidationResult.ERROR, ValidateQuantityNumber().invoke(0))
        assertEquals(ValidationResult.ERROR, ValidateQuantityNumber().invoke(-1))
    }
}