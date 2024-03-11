package hu.tb.minichefy.data.data_source.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
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

    @Query("SELECT * FROM FoodEntity WHERE title = :title AND unitOfMeasurement = :unitOfMeasurement")
    suspend fun searchFoodByDishProperties(title: String, unitOfMeasurement: UnitOfMeasurement): FoodEntity?

    @Query("DELETE FROM FoodEntity WHERE id = :id")
    suspend fun deleteFoodEntity(id: Long): Int

    //tag
    @Query("SELECT * FROM FoodTagEntity")
    fun getAllFoodTag(): Flow<List<FoodTagEntity>>

    @Query("SELECT * FROM foodtagentity WHERE id = :id")
    suspend fun getTagById(id: Long): FoodTagEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrModifyFoodTag(foodTagEntity: FoodTagEntity): Long

    @Query("DELETE FROM FoodTagEntity WHERE id = :id")
    suspend fun deleteFoodTag(id: Long): Int
}