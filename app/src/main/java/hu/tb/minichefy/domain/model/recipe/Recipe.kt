package hu.tb.minichefy.domain.model.recipe

import hu.tb.minichefy.domain.model.recipe.entity.RecipeEntity

data class Recipe(
    val id: Long? = null,
    val icon: Int,
    val title: String,
    val quantity: Int,
    val howToSteps: List<RecipeStep>
)

fun Recipe.toRecipeEntity() = run {
    RecipeEntity(
        icon = icon,
        title = title,
        quantity = quantity,
        recipeId = id
    )
}


