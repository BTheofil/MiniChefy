package hu.tb.minichefy.domain.model

import hu.tb.minichefy.domain.model.entity.RecipeStepEntity

data class RecipeStep(
    val id: Int? = null,
    val step: String
)

fun RecipeStep.toRecipeStepEntity(recipeEntityId: Int) =
    RecipeStepEntity(
        step = this.step,
        recipeEntityId = recipeEntityId,
        recipeStepId = null
    )
