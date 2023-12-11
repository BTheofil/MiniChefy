package hu.tb.minichefy.domain.model

import hu.tb.minichefy.domain.model.entity.RecipeEntity

data class Recipe(
    val id: Long? = null,
    val name: String,
    val quantity: Int,
    val howToSteps: List<RecipeStep>
)

fun Recipe.toRecipeEntity() =
    RecipeEntity(
        title = this.name,
        quantity = this.quantity,
        recipeId = this.id
    )

