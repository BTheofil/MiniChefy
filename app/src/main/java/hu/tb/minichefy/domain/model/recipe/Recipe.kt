package hu.tb.minichefy.domain.model.recipe

import hu.tb.minichefy.domain.model.recipe.entity.RecipeEntity

data class Recipe(
    val id: Long? = null,
    val icon: Int,
    val title: String,
    val quantity: Int,
    val timeToCreate: Float,
    val timeUnit: TimeUnit,
    val howToSteps: List<RecipeStep>
) {
    fun toRecipeEntity() = run {
        RecipeEntity(
            icon = icon,
            title = title,
            quantity = quantity,
            recipeId = id,
            timeToCreate = timeToCreate,
            timeUnit = timeUnit
        )
    }
}

enum class TimeUnit(val id: Int) {
    MINUTES(1),
    HOUR(2)
}


