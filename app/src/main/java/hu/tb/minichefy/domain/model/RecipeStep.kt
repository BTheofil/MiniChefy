package hu.tb.minichefy.domain.model

import hu.tb.minichefy.domain.model.entity.RecipeStepEntity

data class RecipeStep(
    val id: Long? = null,
    val step: String
)

fun RecipeStep.toRecipeStepEntity(recipeEntityId: Long) =
    RecipeStepEntity(
        step = this.step,
        recipeEntityId = recipeEntityId,
        recipeStepId = this.id
    )
