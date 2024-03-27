package hu.tb.minichefy.data.data_source.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import hu.tb.minichefy.domain.model.storage.entity.TagEntity
import hu.tb.minichefy.domain.model.storage.entity.FoodEntity
import hu.tb.minichefy.domain.model.storage.entity.FoodWithTags
import hu.tb.minichefy.domain.model.storage.entity.SimplerFoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StorageDAO {

    //food
    @Transaction
    @Query("SELECT * FROM FoodEntity")
    fun getAllStorageFood(): Flow<List<FoodWithTags>>

    @Transaction
    @Query("SELECT foodId, title FROM FoodEntity")
    suspend fun getAllStorageFoodName(): List<SimplerFoodEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrModifyFood(foodEntity: FoodEntity): Long

    @Transaction
    @Query("SELECT * FROM FoodEntity WHERE title = :title")
    suspend fun searchFoodByTitle(title: String): FoodWithTags?

    @Transaction
    @Query("SELECT foodId, title FROM FoodEntity WHERE title LIKE :searchTitle")
    suspend fun searchProductByTitle(searchTitle: String): List<SimplerFoodEntity>

    @Transaction
    @Query("DELETE FROM FoodEntity WHERE foodId = :id")
    suspend fun deleteFoodEntity(id: Long): Int

    //tag
    @Query("SELECT * FROM TagEntity")
    fun getAllFoodTagFlow(): Flow<List<TagEntity>>

    @Query("SELECT * FROM TagEntity")
    suspend fun getAllFoodTag(): List<TagEntity>

    @Query("SELECT * FROM TagEntity WHERE tagId = :id")
    suspend fun getTagById(id: Long): TagEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrModifyFoodTag(tagEntity: TagEntity): Long

    @Query("DELETE FROM TagEntity WHERE tagId = :id")
    suspend fun deleteFoodTag(id: Long): Int
}