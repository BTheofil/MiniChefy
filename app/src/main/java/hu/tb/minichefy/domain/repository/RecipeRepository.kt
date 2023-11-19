package hu.tb.minichefy.domain.repository

import hu.tb.minichefy.domain.model.Recipe

interface RecipeRepository {

    fun getAllRecipe(): List<Recipe>

    fun getRecipeById(id: Int): Recipe
}