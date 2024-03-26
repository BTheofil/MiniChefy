package hu.tb.minichefy.domain.model.recipe

import hu.tb.minichefy.domain.model.recipe.entity.FoodEntityWrapper
import hu.tb.minichefy.domain.model.recipe.entity.RecipeEntity
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
) {
    fun toRecipeEntity() = run {
        RecipeEntity(
            icon = icon,
            title = title,
            quantity = quantity,
            recipeId = id,
            timeToCreate = timeToCreate,
            timeUnit = timeUnit,
            ingredientList = FoodEntityWrapper(
                ingredients = ingredientList.map {
                    it.toFoodEntity()
                }
            )
        )
    }
}

enum class TimeUnit(val id: Int) {
    MINUTES(1),
    HOUR(2)
}


