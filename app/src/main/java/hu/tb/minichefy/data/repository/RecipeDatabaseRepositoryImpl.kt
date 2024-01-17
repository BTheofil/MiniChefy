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

    override fun getAllRecipe(): Flow<List<Recipe>> =
        dao.getAllRecipe().map { recipeHowToCreateLists ->
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

    override suspend fun getRecipeById(id: Long): Flow<Recipe> =
        dao.getRecipeById(id).map { recipeHowToCreateList ->
            Recipe(
                id = recipeHowToCreateList.recipeEntity.recipeId,
                name = recipeHowToCreateList.recipeEntity.title,
                howToSteps = recipeHowToCreateList.howToStepsList.map {
                    RecipeStep(
                        it.recipeStepId,
                        it.step
                    )
                },
                quantity = recipeHowToCreateList.recipeEntity.quantity
            )
        }

    override suspend fun saveRecipe(recipe: Recipe): Long =
        dao.insertRecipe(recipe.toRecipeEntity())

    override suspend fun saveStep(step: RecipeStep, recipeEntityId: Long): Long =
        dao.insertStep(step.toRecipeStepEntity(recipeEntityId))

    suspend fun deleteRecipe(id: Long){
        dao.deleteRecipe(id)
    }
}