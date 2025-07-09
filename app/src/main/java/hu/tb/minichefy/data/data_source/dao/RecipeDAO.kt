package hu.tb.minichefy.data.data_source.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import hu.tb.minichefy.domain.model.recipe.entity.RecipeEntity
import hu.tb.minichefy.domain.model.recipe.entity.RecipeBlock
import hu.tb.minichefy.domain.model.recipe.entity.RecipeIngredientEntity
import hu.tb.minichefy.domain.model.recipe.entity.RecipeStepEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDAO {

    @Transaction
    @Query("SELECT * FROM RecipeEntity")
    fun getAllRecipe(): Flow<List<RecipeBlock>>

    @Transaction
    @Query("SELECT * FROM RecipeEntity WHERE recipeId = :recipeId")
    fun getRecipeById(recipeId: Long): Flow<RecipeBlock>

    @Transaction
    @Query("SELECT * FROM RecipeEntity WHERE title LIKE :searchTitle")
    fun getRecipeByTitle(searchTitle: String): Flow<List<RecipeBlock>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeEntity(recipeEntity: RecipeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStepEntity(stepEntity: RecipeStepEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredientEntity(ingredientEntity: RecipeIngredientEntity): Long

    @Transaction
    @Query("DELETE FROM RecipeEntity WHERE recipeId = :id")
    fun deleteRecipe(id: Long)
}