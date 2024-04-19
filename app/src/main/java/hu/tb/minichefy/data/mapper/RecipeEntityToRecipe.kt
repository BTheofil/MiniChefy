package hu.tb.minichefy.data.mapper

import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.recipe.RecipeIngredient
import hu.tb.minichefy.domain.model.recipe.RecipeStep
import hu.tb.minichefy.domain.model.recipe.entity.RecipeBlock

class RecipeEntityToRecipe {

    fun map(from: RecipeBlock): Recipe = from.run {
        Recipe(
            id = recipeEntity.recipeId,
            icon = recipeEntity.icon,
            title = recipeEntity.title,
            quantity = recipeEntity.quantity,
            howToSteps = stepList.map {
                RecipeStep(
                    id = it.recipeStepId,
                    step = it.step
                )
            },
            timeToCreate = recipeEntity.timeToCreate,
            timeUnit = recipeEntity.timeUnit,
            ingredientList = ingredientList.map {
                RecipeIngredient(
                    id = it.ingredientId,
                    title = it.title,
                    quantity = it.quantity,
                    unitOfMeasurement = it.unitOfMeasurement
                )
            }
        )
    }
}