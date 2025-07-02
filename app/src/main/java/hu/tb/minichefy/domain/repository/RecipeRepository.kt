package hu.tb.minichefy.domain.repository

import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.recipe.RecipeIngredient
import hu.tb.minichefy.domain.model.recipe.RecipeStep
import hu.tb.minichefy.domain.model.recipe.TimeUnit
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    fun getAllRecipe(): Flow<List<Recipe>>

    suspend fun getRecipeById(id: Long): Flow<Recipe>

    suspend fun saveRecipe(
        id: Long? = null,
        icon: String,
        title: String,
        quantity: Int,
        timeToCreate: Int,
        timeUnit: TimeUnit,
    ): Long

    suspend fun saveStep(step: RecipeStep, recipeEntityId: Long): Long

    suspend fun saveIngredient(
        ingredient: RecipeIngredient,
        recipeEntityId: Long
    ): Long

    suspend fun searchRecipeTitle(text: String): Flow<List<Recipe>>

    suspend fun deleteRecipe(id: Long)
}