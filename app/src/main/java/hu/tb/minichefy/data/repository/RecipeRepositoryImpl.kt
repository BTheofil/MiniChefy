package hu.tb.minichefy.data.repository

import hu.tb.minichefy.data.data_source.RecipeDataSource
import hu.tb.minichefy.domain.model.Recipe
import hu.tb.minichefy.domain.repository.RecipeRepository
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val dataSource: RecipeDataSource
) : RecipeRepository {

    override fun getAllRecipe(): List<Recipe> =
        dataSource.getAllRecipe()


    override fun getRecipeById(id: Int): Recipe =
        dataSource.getRecipeById(id)

}