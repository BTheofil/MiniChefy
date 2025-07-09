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

@Dao
interface StorageDAO {

    //food
    @Transaction
    @Query("SELECT foodId, title, unitOfMeasurement FROM FoodEntity WHERE foodId NOT IN (SELECT foodId FROM FoodAndTagsCrossRef WHERE tagId = :excludedTagId)")
    fun getSimpleFoodExcludedTag(excludedTagId: Long): Flow<List<SimpleFoodEntity>>

    @Transaction
    @Query("SELECT * FROM FoodEntity")
    fun getAllFoodEntity(): Flow<List<FoodWithTags>>

    @Transaction
    @Query("SELECT * FROM FoodEntity WHERE title = :title")
    fun searchFoodByTitle(title: String): Flow<List<FoodWithTags>>

    @Transaction
    @Query("SELECT foodId, title, unitOfMeasurement FROM FoodEntity WHERE title LIKE :searchTitle AND foodId NOT IN (SELECT foodId FROM FoodAndTagsCrossRef WHERE tagId = :excludedTagId)")
    fun searchSimpleFoodsByTitle(searchTitle: String, excludedTagId: Long): Flow<List<SimpleFoodEntity>>

    @Transaction
    @Query("SELECT * FROM FoodEntity WHERE foodId IN (SELECT foodId FROM FoodAndTagsCrossRef WHERE tagId IN (:tagIds))")
    fun searchFoodsByTag(tagIds: List<Long>): Flow<List<FoodWithTags>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodEntity(foodEntity: FoodEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodTagCrossRef(crossRef: FoodAndTagsCrossRef): Long

    @Transaction
    @Query("DELETE FROM FoodAndTagsCrossRef WHERE foodId = :foodId")
    fun deleteFoodWithTagsConnectionByFoodId(foodId: Long)

    @Transaction
    @Query("DELETE FROM FoodAndTagsCrossRef WHERE foodId = :foodId AND tagId = :tagId")
    fun deleteFoodTagCrossRef(foodId: Long, tagId: Long)

    @Transaction
    @Query("DELETE FROM FoodEntity WHERE foodId = :id")
    fun deleteFoodEntity(id: Long)

    //tag
    @Query("SELECT * FROM TagEntity")
    fun getAllFoodTagFlow(): Flow<List<TagEntity>>

    @Query("SELECT * FROM TagEntity WHERE tagId = :id")
    fun getTagById(id: Long): TagEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTagEntity(tagEntity: TagEntity): Long

    @Query("DELETE FROM TagEntity WHERE tagId = :id")
    fun deleteFoodTag(id: Long)
}