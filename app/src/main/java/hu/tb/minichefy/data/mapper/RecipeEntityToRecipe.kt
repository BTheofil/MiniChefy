package hu.tb.minichefy.data.mapper

import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.recipe.RecipeStep
import hu.tb.minichefy.domain.model.recipe.entity.RecipeWithSteps

class RecipeEntityToRecipe {

    fun map(from: RecipeWithSteps): Recipe = from.run {
        Recipe(
            id = recipeEntity.recipeId,
            icon = recipeEntity.icon,
            title = recipeEntity.title,
            quantity = recipeEntity.quantity,
            howToSteps = recipeSteps.map {
                RecipeStep(
                    id = it.recipeStepId,
                    step = it.step
                )
            },
            timeToCreate = recipeEntity.timeToCreate,
            timeUnit = recipeEntity.timeUnit
        )
    }
}