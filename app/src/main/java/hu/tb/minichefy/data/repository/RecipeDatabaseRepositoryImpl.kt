package hu.tb.minichefy.data.repository

import hu.tb.minichefy.data.data_source.db.RecipeDAO
import hu.tb.minichefy.domain.model.Recipe
import hu.tb.minichefy.domain.model.RecipeStep
import hu.tb.minichefy.domain.model.toRecipeEntity
import hu.tb.minichefy.domain.model.toRecipeStepEntity
import hu.tb.minichefy.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecipeDatabaseRepositoryImpl @Inject constructor(
    private val dao: RecipeDAO
) : RecipeRepository {

    override fun getAllRecipe(): Flow<List<Recipe>> {
        return dao.getAllRecipe().map { recipeHowToCreateLists ->
            recipeHowToCreateLists.map { recipeHowToCreateList ->
                Recipe(
                    recipeHowToCreateList.recipeEntity.recipeId,
                    recipeHowToCreateList.recipeEntity.title,
                    recipeHowToCreateList.recipeEntity.quantity,
                    howToSteps = recipeHowToCreateList.howToStepsList.map {
                        RecipeStep(
                            it.recipeStepId,
                            it.step
                        )
                    }
                )
            }
        }
    }



    override fun getRecipeById(id: Int): Recipe {
        TODO("Not yet implemented")
    }

    override suspend fun saveRecipe(recipe: Recipe): Long =
        dao.insertRecipe(recipe.toRecipeEntity())

    override suspend fun saveStep(step: RecipeStep, recipeEntityId: Int): Long =
        dao.insertStep(step.toRecipeStepEntity(recipeEntityId))
}