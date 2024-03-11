package hu.tb.minichefy.data.repository

import hu.tb.minichefy.data.data_source.dao.RecipeDAO
import hu.tb.minichefy.data.mapper.RecipeEntityToRecipe
import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.recipe.RecipeStep
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

    override suspend fun saveRecipe(recipe: Recipe): Long =
        dao.insertRecipe(recipe.toRecipeEntity())

    override suspend fun saveStep(step: RecipeStep, recipeEntityId: Long): Long =
        dao.insertStep(step.toRecipeStepEntity(recipeEntityId))

    override suspend fun deleteRecipe(id: Long): Int = dao.deleteRecipe(id)

    override suspend fun searchRecipeByTitle(searchTitle: String): List<Recipe> {
        val recipes = dao.searchRecipeByTitle("%$searchTitle%")
        return recipes.map {
            RecipeEntityToRecipe().map(it)
        }
    }

}