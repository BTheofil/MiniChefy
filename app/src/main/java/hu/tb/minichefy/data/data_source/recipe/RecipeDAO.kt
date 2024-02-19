package hu.tb.minichefy.data.data_source.recipe

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import hu.tb.minichefy.domain.model.recipe.entity.RecipeEntity
import hu.tb.minichefy.domain.model.recipe.entity.RecipeWithSteps
import hu.tb.minichefy.domain.model.recipe.entity.RecipeStepEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDAO {

    @Transaction
    @Query("SELECT * FROM RecipeEntity")
    fun getAllRecipe(): Flow<List<RecipeWithSteps>>

    @Transaction
    @Query("SELECT * FROM RecipeEntity WHERE recipeId = :recipeId")
    suspend fun getRecipeById(recipeId: Long): RecipeWithSteps

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeEntity: RecipeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStep(stepEntity: RecipeStepEntity): Long

    @Transaction
    @Query("DELETE FROM RecipeEntity WHERE recipeId = :id")
    suspend fun deleteRecipe(id: Long): Int

    @Transaction
    @Query("SELECT * FROM RecipeEntity WHERE title LIKE :searchTitle")
    suspend fun searchRecipeByTitle(searchTitle: String): List<RecipeWithSteps>
}