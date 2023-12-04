package hu.tb.minichefy.data.repository

import hu.tb.minichefy.data.data_source.db.RecipeDAO
import hu.tb.minichefy.domain.model.Recipe
import hu.tb.minichefy.domain.model.toRecipeEntity
import hu.tb.minichefy.domain.repository.RecipeRepository
import javax.inject.Inject

class RecipeDatabaseRepositoryImpl @Inject constructor(
    private val dao: RecipeDAO
): RecipeRepository {

    override fun getAllRecipe(): List<Recipe> {
        return emptyList()
    }

    override fun getRecipeById(id: Int): Recipe {
        TODO("Not yet implemented")
    }

    override suspend fun saveRecipe(recipe: Recipe): Long =
        dao.insertRecipe(recipe.toRecipeEntity())
}