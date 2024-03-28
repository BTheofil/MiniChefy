package hu.tb.minichefy.domain.model.recipe

import hu.tb.minichefy.domain.model.recipe.entity.RecipeStepEntity

data class RecipeStep(
    val id: Long? = null,
    val step: String
){
    fun toRecipeStepEntity(recipeEntityId: Long) =
        RecipeStepEntity(
            step = step,
            recipeEntityId = recipeEntityId,
            recipeStepId = id
        )
}
