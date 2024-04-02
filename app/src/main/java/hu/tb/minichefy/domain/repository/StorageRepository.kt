package hu.tb.minichefy.domain.repository

import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodSummary
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import kotlinx.coroutines.flow.Flow

interface StorageRepository {

    //food
    fun getAllFood(): Flow<List<Food>>

    fun getKnownFoods(): Flow<List<Food>>

    suspend fun getAllStorageFoodName(): List<FoodSummary>

    suspend fun searchFoodByTitle(title: String): Food?

    suspend fun searchProductByTitle(searchText: String): List<FoodSummary>

    suspend fun saveOrModifyFood(
        id: Long? = null,
        title: String,
        icon: Int,
        quantity: Float,
        unitOfMeasurement: UnitOfMeasurement
    ): Long

    suspend fun saveFoodAndTag(foodId: Long, tagId: Long): Long

    suspend fun deleteFoodAndTagsByFoodId(foodId: Long): Int

    suspend fun deleteFoodAndTag(foodId: Long, tagId: Long): Int

    suspend fun deleteFoodById(id: Long): Int

    //tag
    fun getAllFoodTag(): Flow<List<FoodTag>>

    suspend fun getFilterableTagList(): List<FoodTag>

    suspend fun getTagById(id: Long): FoodTag

    suspend fun saveOrModifyFoodTag(tag: FoodTag): Long

    suspend fun deleteFoodTag(id: Long): Int
}