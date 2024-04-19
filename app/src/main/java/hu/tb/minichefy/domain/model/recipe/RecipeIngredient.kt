package hu.tb.minichefy.domain.model.recipe

import hu.tb.minichefy.domain.model.recipe.entity.RecipeIngredientEntity
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement

data class RecipeIngredient(
    val id: Long? = null,
    val title: String,
    val quantity: Float,
    val unitOfMeasurement: UnitOfMeasurement
) {
    fun toIngredientEntity(recipeId: Long) =
        RecipeIngredientEntity(
            recipeEntityId = recipeId,
            ingredientId = id,
            title = title,
            quantity = quantity,
            unitOfMeasurement = unitOfMeasurement
        )

}
