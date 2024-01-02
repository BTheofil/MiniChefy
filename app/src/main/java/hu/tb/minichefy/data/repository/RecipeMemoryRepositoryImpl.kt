package hu.tb.minichefy.data.repository

import hu.tb.minichefy.data.data_source.memory.RecipeDataSource
import hu.tb.minichefy.domain.model.Recipe
import hu.tb.minichefy.domain.model.RecipeStep
import hu.tb.minichefy.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecipeMemoryRepositoryImpl @Inject constructor(
    private val dataSource: RecipeDataSource
) : RecipeRepository {

    override fun getAllRecipe(): Flow<List<Recipe>> = flow {
        dataSource.getAllRecipe()
    }


    override suspend fun getRecipeById(id: Long): Flow<Recipe> = flow {
        dataSource.getRecipeById(id)
    }

    override suspend fun saveRecipe(recipe: Recipe): Long {
        TODO("Not yet implemented")
    }

    override suspend fun saveStep(step: RecipeStep, recipeEntityId: Long): Long {
        TODO("Not yet implemented")
    }

}