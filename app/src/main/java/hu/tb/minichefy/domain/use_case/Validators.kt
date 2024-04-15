package hu.tb.minichefy.domain.use_case

class ValidateCountInteger {
    operator fun invoke(integer: Int): ValidationResult =
        if (integer <= 0) {
            ValidationResult.ERROR
        } else {
            ValidationResult.SUCCESS
        }
}

class ValidateQuantity {
    operator fun invoke(float: Float): ValidationResult =
        if (float.isNaN() || float <= 0f) {
            ValidationResult.ERROR
        } else {
            ValidationResult.SUCCESS
        }
}

class ValidateNumberKeyboard {
    operator fun invoke(input: String): ValidationResult =
        if (!Regex("""^(?!\.)\d*\.?\d*${'$'}""").matches(input) ||
            input.contains("-") ||
            input.contains(" ") ||
            input.contains(
                ","
            )
        ) {
            ValidationResult.ERROR
        } else {
            ValidationResult.SUCCESS
        }
}

class ValidateTextField {
    operator fun invoke(text: String?): ValidationResult =
        if (text.isNullOrEmpty() || text.isBlank()) {
            ValidationResult.ERROR
        } else {
            ValidationResult.SUCCESS
        }
}

enum class ValidationResult {
    SUCCESS,
    ERROR
}