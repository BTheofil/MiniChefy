package hu.tb.minichefy.data.repository

import hu.tb.minichefy.data.data_source.dao.DeletedRecipeCount
import hu.tb.minichefy.data.data_source.dao.IngredientId
import hu.tb.minichefy.data.data_source.dao.RecipeDAO
import hu.tb.minichefy.data.data_source.dao.RecipeId
import hu.tb.minichefy.data.data_source.dao.StepId
import hu.tb.minichefy.data.mapper.RecipeEntityToRecipe
import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.recipe.RecipeIngredient
import hu.tb.minichefy.domain.model.recipe.RecipeStep
import hu.tb.minichefy.domain.model.recipe.TimeUnit
import hu.tb.minichefy.domain.model.recipe.entity.RecipeEntity
import hu.tb.minichefy.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecipeDatabaseRepositoryImpl @Inject constructor(
    private val dao: RecipeDAO
) : RecipeRepository {

    override fun getAllRecipe(): Flow<List<Recipe>> =
        dao.getAllRecipe().map { recipeHowToCreateLists ->
            recipeHowToCreateLists.map {
                RecipeEntityToRecipe().map(it)
            }
        }


    override suspend fun getRecipeById(id: Long): Recipe =
        RecipeEntityToRecipe().map(dao.getRecipeById(id))

    override suspend fun saveRecipe(
        id: Long?,
        icon: Int,
        title: String,
        quantity: Int,
        timeToCreate: Int,
        timeUnit: TimeUnit,
    ): RecipeId {
        val temp = RecipeEntity(
            recipeId = id,
            icon = icon,
            title = title,
            quantity = quantity,
            timeToCreate = timeToCreate,
            timeUnit = timeUnit
        )

        return dao.insertRecipeEntity(temp)
    }

    override suspend fun saveStep(step: RecipeStep, recipeEntityId: Long): StepId =
        dao.insertStepEntity(step.toRecipeStepEntity(recipeEntityId))

    override suspend fun saveIngredient(
        ingredient: RecipeIngredient,
        recipeEntityId: Long
    ): IngredientId =
        dao.insertIngredientEntity(ingredient.toIngredientEntity(recipeEntityId))

    override suspend fun searchRecipeByTitle(searchTitle: String): List<Recipe> {
        val recipes = dao.searchRecipeByTitle("%$searchTitle%")
        return recipes.map {
            RecipeEntityToRecipe().map(it)
        }
    }

    override suspend fun deleteRecipe(id: Long): DeletedRecipeCount = dao.deleteRecipe(id)
}