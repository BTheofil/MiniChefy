package hu.tb.minichefy.data.data_source.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import hu.tb.minichefy.domain.model.entity.RecipeEntity
import hu.tb.minichefy.domain.model.entity.RecipeHowToCreateList
import hu.tb.minichefy.domain.model.entity.RecipeStepEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDAO {

    @Query("SELECT * FROM RecipeEntity")
    fun getRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM RecipeEntity WHERE recipeId = :id")
    suspend fun getRecipeById(id: Int): RecipeEntity?

    @Query("SELECT * FROM RecipeEntity")
    fun getAllRecipe(): Flow<List<RecipeHowToCreateList>>

    @Transaction
    @Query("SELECT * FROM RecipeEntity WHERE recipeId = :recipeId")
    fun getRecipeStepsListById(recipeId: Int): Flow<List<RecipeHowToCreateList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeEntity: RecipeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStep(stepEntity: RecipeStepEntity): Long

    @Delete
    suspend fun deleteRecipe(recipeEntity: RecipeEntity)
}