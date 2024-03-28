package hu.tb.minichefy.data.data_source.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import hu.tb.minichefy.domain.model.recipe.entity.RecipeEntity
import hu.tb.minichefy.domain.model.recipe.entity.RecipeBlock
import hu.tb.minichefy.domain.model.recipe.entity.RecipeFoodCrossRef
import hu.tb.minichefy.domain.model.recipe.entity.RecipeStepEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDAO {

    @Transaction
    @Query("SELECT * FROM RecipeEntity")
    fun getAllRecipe(): Flow<List<RecipeBlock>>

    @Transaction
    @Query("SELECT * FROM RecipeEntity WHERE recipeId = :recipeId")
    suspend fun getRecipeById(recipeId: Long): RecipeBlock

    @Transaction
    @Query("SELECT * FROM RecipeEntity WHERE title LIKE :searchTitle")
    suspend fun searchRecipeByTitle(searchTitle: String): List<RecipeBlock>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeEntity(recipeEntity: RecipeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStepEntity(stepEntity: RecipeStepEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeIngredientCrossRef(crossRef: RecipeFoodCrossRef): Long

    @Transaction
    @Query("DELETE FROM RecipeEntity WHERE recipeId = :id")
    suspend fun deleteRecipe(id: Long): Int
}