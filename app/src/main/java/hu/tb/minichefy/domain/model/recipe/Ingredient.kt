package hu.tb.minichefy.domain.model.recipe

import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement

data class IngredientSimple(
    val id: Long,
    val title: String
)

data class RecipeIngredient(
    val id: Long?,
    val title: String,
    val quantity: Float,
    val unitOfMeasurement: UnitOfMeasurement,
)
