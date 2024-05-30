package hu.tb.minichefy.domain.exceptions

data class NotCompatibleCalculation(
    override val message: String,
    val argument: String? = null
): Exception()