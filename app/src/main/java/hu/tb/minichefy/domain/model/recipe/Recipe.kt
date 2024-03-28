package hu.tb.minichefy.domain.model.recipe

import hu.tb.minichefy.domain.model.storage.Food

data class Recipe(
    val id: Long? = null,
    val icon: Int,
    val title: String,
    val quantity: Int,
    val timeToCreate: Int,
    val timeUnit: TimeUnit,
    val howToSteps: List<RecipeStep>,
    val ingredientList: List<Food>
)

enum class TimeUnit(val id: Int) {
    MINUTES(1),
    HOUR(2)
}


