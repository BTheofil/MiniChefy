package hu.tb.minichefy.domain.repository

import hu.tb.minichefy.data.data_source.dao.DeletedRecipeCount
import hu.tb.minichefy.data.data_source.dao.IngredientId
import hu.tb.minichefy.data.data_source.dao.RecipeId
import hu.tb.minichefy.data.data_source.dao.StepId
import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.recipe.RecipeIngredient
import hu.tb.minichefy.domain.model.recipe.RecipeStep
import hu.tb.minichefy.domain.model.recipe.TimeUnit
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    fun getAllRecipe(): Flow<List<Recipe>>

    suspend fun getRecipeById(id: Long): Recipe

    suspend fun saveRecipe(
        id: Long? = null,
        icon: Int,
        title: String,
        quantity: Int,
        timeToCreate: Int,
        timeUnit: TimeUnit,
    ): RecipeId

    suspend fun saveStep(step: RecipeStep, recipeEntityId: Long): StepId

    suspend fun saveIngredient(
        ingredient: RecipeIngredient,
        recipeEntityId: Long
    ): IngredientId

    suspend fun searchRecipeByTitle(searchTitle: String): List<Recipe>

    suspend fun deleteRecipe(id: Long): DeletedRecipeCount
}