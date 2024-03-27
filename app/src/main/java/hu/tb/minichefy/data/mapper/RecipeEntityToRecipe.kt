package hu.tb.minichefy.data.mapper

import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.recipe.RecipeStep
import hu.tb.minichefy.domain.model.recipe.entity.RecipeBlock

class RecipeEntityToRecipe {

    fun map(from: RecipeBlock): Recipe = from.run {
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
            timeUnit = recipeEntity.timeUnit,
            ingredientList = ingredients.map { FoodEntityToFood().map(it) }
        )
    }
}