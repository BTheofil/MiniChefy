package hu.tb.minichefy.domain.model

import hu.tb.minichefy.domain.model.entity.RecipeEntity
import hu.tb.minichefy.presentation.screens.recipe.components.IconResource

data class Recipe(
    val id: Long? = null,
    val icon: IconResource? = null,
    val title: String,
    val quantity: Int,
    val howToSteps: List<RecipeStep>
)

fun Recipe.toRecipeEntity() =
    RecipeEntity(
        title = this.title,
        quantity = this.quantity,
        recipeId = this.id
    )

