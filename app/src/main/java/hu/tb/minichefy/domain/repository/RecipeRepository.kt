package hu.tb.minichefy.domain.repository

import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.recipe.RecipeStep
import hu.tb.minichefy.domain.model.recipe.TimeUnit
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    fun getAllRecipe(): Flow<List<Recipe>>

    suspend fun getRecipeById(id: Long): Recipe

    suspend fun saveOrModifyRecipe(
        id: Long? = null,
        icon: Int,
        title: String,
        quantity: Int,
        timeToCreate: Int,
        timeUnit: TimeUnit,
    ): Long

    suspend fun saveStep(step: RecipeStep, recipeEntityId: Long): Long

    suspend fun deleteRecipe(id: Long): Int

    suspend fun searchRecipeByTitle(searchTitle: String): List<Recipe>

    suspend fun saveRecipeIngredientCrossRef(recipeId: Long, foodId: Long): Long
}