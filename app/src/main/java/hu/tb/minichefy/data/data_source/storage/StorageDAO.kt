package hu.tb.minichefy.data.data_source.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hu.tb.minichefy.domain.model.storage.entity.FoodTagEntity
import hu.tb.minichefy.domain.model.storage.entity.FoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StorageDAO {

    //food
    @Query("SELECT * FROM FoodEntity")
    fun getAllStorageFood(): Flow<List<FoodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrModifyFood(foodEntity: FoodEntity): Long

    @Query("DELETE FROM FoodEntity WHERE id = :id")
    suspend fun deleteFoodEntity(id: Long): Int

    //tag
    @Query("SELECT * FROM FoodTagEntity")
    fun getAllFoodTag(): Flow<List<FoodTagEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrModifyFoodTag(foodTagEntity: FoodTagEntity): Long

    @Query("DELETE FROM FoodTagEntity WHERE id = :id")
    suspend fun deleteFoodTag(id: Long): Int
}