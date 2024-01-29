package hu.tb.minichefy.domain.use_case

class ValidateQuantityNumber {

    operator fun invoke(quantityNumber: Int): ValidationResult =
        if (quantityNumber < 1) {
            ValidationResult.ERROR
        } else {
            ValidationResult.SUCCESS
        }
}

enum class ValidationResult {
    SUCCESS,
    ERROR
}