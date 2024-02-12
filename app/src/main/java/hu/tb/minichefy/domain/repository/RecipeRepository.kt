package hu.tb.minichefy.domain.repository

import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.recipe.RecipeStep
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    fun getAllRecipe(): Flow<List<Recipe>>

    suspend fun getRecipeById(id: Long): Recipe

    suspend fun saveRecipe(recipe: Recipe): Long

    suspend fun saveStep(step: RecipeStep, recipeEntityId: Long): Long

    suspend fun deleteRecipe(id: Long): Int

    suspend fun searchRecipeByTitle(searchTitle: String): List<Recipe>
}