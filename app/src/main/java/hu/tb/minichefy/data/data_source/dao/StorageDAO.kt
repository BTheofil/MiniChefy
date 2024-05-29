package hu.tb.minichefy.data.data_source.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import hu.tb.minichefy.domain.model.storage.entity.FoodAndTagsCrossRef
import hu.tb.minichefy.domain.model.storage.entity.TagEntity
import hu.tb.minichefy.domain.model.storage.entity.FoodEntity
import hu.tb.minichefy.domain.model.storage.entity.FoodWithTags
import hu.tb.minichefy.domain.model.storage.entity.SimpleFoodEntity
import kotlinx.coroutines.flow.Flow

typealias FoodId = Long

@Dao
interface StorageDAO {

    //food
    @Transaction
    @Query("SELECT foodId, title, unitOfMeasurement FROM FoodEntity WHERE foodId NOT IN (SELECT foodId FROM FoodAndTagsCrossRef WHERE tagId = :excludedTagId)")
    suspend fun getSimpleFoodExcludedTag(excludedTagId: Long): List<SimpleFoodEntity>

    @Transaction
    @Query("SELECT * FROM FoodEntity")
    fun getFoodWithTagsFlow(): Flow<List<FoodWithTags>>

    @Transaction
    @Query("SELECT * FROM FoodEntity")
    suspend fun getFoodWithTagsList(): List<FoodWithTags>

    @Transaction
    @Query("SELECT * FROM FoodEntity WHERE title = :title")
    suspend fun searchFoodByTitle(title: String): List<FoodWithTags>

    @Transaction
    @Query("SELECT foodId, title, unitOfMeasurement FROM FoodEntity WHERE title LIKE :searchTitle AND foodId NOT IN (SELECT foodId FROM FoodAndTagsCrossRef WHERE tagId = :excludedTagId)")
    suspend fun searchSimpleFoodsByTitle(searchTitle: String, excludedTagId: Long): List<SimpleFoodEntity>

    @Transaction
    @Query("SELECT * FROM FoodEntity WHERE foodId IN (SELECT foodId FROM FoodAndTagsCrossRef WHERE tagId IN (:tagIds))")
    suspend fun searchFoodsByTag(tagIds: List<Long>): List<FoodWithTags>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodEntity(foodEntity: FoodEntity): FoodId

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodTagCrossRef(crossRef: FoodAndTagsCrossRef): Long

    @Transaction
    @Query("DELETE FROM FoodAndTagsCrossRef WHERE foodId = :foodId")
    suspend fun deleteFoodWithTagsConnectionByFoodId(foodId: Long): Int

    @Transaction
    @Query("DELETE FROM FoodAndTagsCrossRef WHERE foodId = :foodId AND tagId = :tagId")
    suspend fun deleteFoodTagCrossRef(foodId: Long, tagId: Long): Int

    @Transaction
    @Query("DELETE FROM FoodEntity WHERE foodId = :id")
    suspend fun deleteFoodEntity(id: Long): Int

    //tag
    @Query("SELECT * FROM TagEntity")
    fun getAllFoodTagFlow(): Flow<List<TagEntity>>

    @Query("SELECT * FROM TagEntity WHERE tagId = :id")
    suspend fun getTagById(id: Long): TagEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTagEntity(tagEntity: TagEntity): Long

    @Query("DELETE FROM TagEntity WHERE tagId = :id")
    suspend fun deleteFoodTag(id: Long): Int
}