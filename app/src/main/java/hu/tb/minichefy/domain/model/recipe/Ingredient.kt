package hu.tb.minichefy.domain.model.recipe

import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement

sealed interface IngredientBase {
    val id: Long?
    val title: String

    data class UnSelectedIngredient(
        override val id: Long?,
        override val title: String,
    ): IngredientBase

    data class SelectedIngredient(
        override val id: Long?,
        override val title: String,
        val quantity: Float,
        val unitOfMeasurement: UnitOfMeasurement
    ) : IngredientBase
}
