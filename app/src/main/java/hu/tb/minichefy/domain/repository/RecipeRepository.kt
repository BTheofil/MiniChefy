package hu.tb.minichefy.domain.repository

import hu.tb.minichefy.domain.model.Recipe
import hu.tb.minichefy.domain.model.RecipeStep
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    fun getAllRecipe(): Flow<List<Recipe>>

    suspend fun getRecipeById(id: Long): Flow<Recipe>

    suspend fun saveRecipe(recipe: Recipe): Long

    suspend fun saveStep(step: RecipeStep, recipeEntityId: Long): Long
}