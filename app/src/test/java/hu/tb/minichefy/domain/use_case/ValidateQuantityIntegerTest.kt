package hu.tb.minichefy.domain.use_case

import org.junit.Assert.assertEquals
import org.junit.Test

class ValidateQuantityIntegerTest {

    @Test
    fun `Check Quantity validator works currently`() {
        assertEquals(ValidationResult.SUCCESS, ValidateQuantityInteger().invoke(2))
        assertEquals(ValidationResult.SUCCESS, ValidateQuantityInteger().invoke(1))
        assertEquals(ValidationResult.ERROR, ValidateQuantityInteger().invoke(0))
        assertEquals(ValidationResult.ERROR, ValidateQuantityInteger().invoke(-1))
    }
}