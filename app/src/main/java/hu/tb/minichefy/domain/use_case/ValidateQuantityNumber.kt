package hu.tb.minichefy.domain.use_case

class ValidateQuantityNumber {

    operator fun invoke(quantityNumber: Int): ValidationResult =
        if (quantityNumber < 1) {
            ValidationResult.ERROR
        } else {
            ValidationResult.SUCCESS
        }
}

class ValidateTextField {
    operator fun invoke(text: String?): ValidationResult =
        if (text.isNullOrEmpty() || text.isBlank()){
            ValidationResult.ERROR
        } else {
            ValidationResult.SUCCESS
        }
}

enum class ValidationResult {
    SUCCESS,
    ERROR
}