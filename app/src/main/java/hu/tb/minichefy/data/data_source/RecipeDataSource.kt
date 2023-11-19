package hu.tb.minichefy.data.data_source

import hu.tb.minichefy.domain.model.Recipe

interface RecipeDataSource {

    fun getAllRecipe(): List<Recipe>

    fun getRecipeById(id: Int): Recipe
}