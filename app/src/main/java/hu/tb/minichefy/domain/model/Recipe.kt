package hu.tb.minichefy.domain.model

import hu.tb.minichefy.domain.model.entity.RecipeEntity

data class Recipe(
    val id: Int,
    val name: String,
    val quantity: Int,
    val howToSteps: List<RecipeStep>
)

fun Recipe.toRecipeEntity() =
    RecipeEntity(
        title = this.name,
        quantity = this.quantity,
        recipeId = null
    )

