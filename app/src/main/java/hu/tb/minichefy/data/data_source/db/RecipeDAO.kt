package hu.tb.minichefy.data.data_source.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hu.tb.minichefy.domain.model.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDAO {

    @Query("SELECT * FROM recipeentity")
    fun getRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipeentity WHERE id = :id")
    suspend fun getRecipeById(id: Int): RecipeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeEntity: RecipeEntity)

    @Delete
    suspend fun deleteRecipe(recipeEntity: RecipeEntity)
}