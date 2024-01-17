package hu.tb.minichefy.data.data_source.db

import androidx.room.Dao
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

    //@Query("SELECT * FROM RecipeEntity WHERE recipeId = :id")
    //suspend fun getRecipeById(id: Long): RecipeEntity

    @Query("SELECT * FROM RecipeEntity")
    fun getAllRecipe(): Flow<List<RecipeHowToCreateList>>

    @Query("SELECT * FROM RecipeEntity WHERE recipeId = :recipeId")
    fun getRecipeById(recipeId: Long): Flow<RecipeHowToCreateList>

    @Transaction
    @Query("SELECT * FROM RecipeStepEntity WHERE recipeEntityId = :recipeId")
    fun getRecipeStepsListById(recipeId: Long): Flow<List<RecipeStepEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeEntity: RecipeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStep(stepEntity: RecipeStepEntity): Long

    @Query("DELETE FROM RecipeEntity WHERE recipeId = :id")
    suspend fun deleteRecipe(id: Long)
}