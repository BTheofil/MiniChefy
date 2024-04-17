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
import hu.tb.minichefy.domain.model.storage.entity.SimplerFoodEntity
import hu.tb.minichefy.domain.model.storage.entity.UNKNOWN_TAG_ID
import kotlinx.coroutines.flow.Flow

@Dao
interface StorageDAO {

    //food
    @Transaction
    @Query("SELECT * FROM FoodEntity")
    fun getAllStorageFood(): Flow<List<FoodWithTags>>

    @Query("SELECT foodId FROM FoodAndTagsCrossRef WHERE tagId = ($UNKNOWN_TAG_ID)")
    fun getUnknownTagFoodIds(): Flow<List<Long>>

    @Transaction
    @Query("SELECT foodId, title FROM FoodEntity")
    suspend fun getAllStorageFoodName(): List<SimplerFoodEntity>

    @Transaction
    @Query("SELECT * FROM FoodEntity f " +
            "WHERE NOT EXISTS (SELECT 1 FROM FoodAndTagsCrossRef WHERE f.foodId = foodId AND tagId = ($UNKNOWN_TAG_ID)) " +
            "OR f.foodId NOT IN (SELECT foodId FROM FoodAndTagsCrossRef)")
    fun getKnownFoodsFlow(): Flow<List<FoodWithTags>>

    @Transaction
    @Query("SELECT * FROM FoodEntity f " +
            "WHERE NOT EXISTS (SELECT 1 FROM FoodAndTagsCrossRef WHERE f.foodId = foodId AND tagId = ($UNKNOWN_TAG_ID)) " +
            "OR f.foodId NOT IN (SELECT foodId FROM FoodAndTagsCrossRef)")
    suspend fun getKnownFoodsList(): List<FoodWithTags>

    @Transaction
    @Query("SELECT * FROM FoodEntity WHERE title = :title")
    suspend fun searchFoodByTitle(title: String): FoodWithTags?

    @Transaction
    @Query("SELECT foodId, title FROM FoodEntity WHERE title LIKE :searchTitle")
    suspend fun searchSimpleFoodsByTitle(searchTitle: String): List<SimplerFoodEntity>

    @Transaction
    @Query("SELECT * FROM FoodEntity WHERE foodId IN (SELECT foodId FROM FoodAndTagsCrossRef WHERE tagId IN (:tagIds))")
    suspend fun searchFoodsByTag(tagIds: List<Long>): List<FoodWithTags>

    @Transaction
    @Query("SELECT * FROM FoodEntity WHERE title LIKE '%' || :foodTitle || '%' AND foodId IN (SELECT foodId FROM FoodAndTagsCrossRef WHERE tagId != ($UNKNOWN_TAG_ID))")
    suspend fun searchKnownFoodByTitle(foodTitle: String): List<FoodWithTags>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodEntity(foodEntity: FoodEntity): Long

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

    @Query("SELECT * FROM TagEntity WHERE tagId NOT IN ($UNKNOWN_TAG_ID)")
    suspend fun getTagsExceptUnknown(): List<TagEntity>

    @Query("SELECT * FROM TagEntity WHERE tagId = :id")
    suspend fun getTagById(id: Long): TagEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTagEntity(tagEntity: TagEntity): Long

    @Query("DELETE FROM TagEntity WHERE tagId = :id")
    suspend fun deleteFoodTag(id: Long): Int
}