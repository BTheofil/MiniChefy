package hu.tb.minichefy.domain.model.recipe

import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement

data class IngredientDraft(
    val id: Long,
    val title: String
)

data class IngredientRecipe(
    val id: Long? = null,
    val title: String,
    val quantity: Float,
    val unitOfMeasurement: UnitOfMeasurement
)

