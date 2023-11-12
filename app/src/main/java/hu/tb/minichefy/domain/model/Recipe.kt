package hu.tb.minichefy.domain.model

data class Recipe(
    val id: Int,
    val name: String,
    val quantity: Int,
    val howToSteps: List<RecipeStep>
)
