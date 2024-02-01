package hu.tb.minichefy.data.mapper

import hu.tb.minichefy.domain.model.Recipe
import hu.tb.minichefy.domain.model.RecipeStep
import hu.tb.minichefy.domain.model.entity.RecipeWithSteps

class RecipeEntityToRecipe {

    fun map(from: RecipeWithSteps): Recipe = from.run {
        Recipe(
            id = this.recipeEntity.recipeId,
            icon = recipeEntity.icon,
            title = this.recipeEntity.title,
            quantity = this.recipeEntity.quantity,
            howToSteps = this.recipeSteps.map {
                RecipeStep(
                    id = it.recipeStepId,
                    step = it.step
                )
            }
        )
    }
}