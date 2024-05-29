package hu.tb.minichefy.domain.repository

import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodSummary
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import kotlinx.coroutines.flow.Flow

interface StorageRepository {

    //food
    fun getKnownFoodsFlow(): Flow<List<Food>>

    suspend fun getStorageIngredients(): List<FoodSummary>

    suspend fun getKnownFoodList(): List<Food>

    suspend fun searchFoodByTitle(title: String): List<Food>

    suspend fun searchIngredientsLByLikelyTitle(searchText: String): List<FoodSummary>

    suspend fun searchFoodsByTag(tagIds: List<Long>): List<Food>

    suspend fun saveOrModifyFood(
        id: Long? = null,
        title: String,
        icon: String,
        quantity: Float,
        unitOfMeasurement: UnitOfMeasurement
    ): Long

    suspend fun saveFoodAndTag(foodId: Long, tagId: Long): Long

    suspend fun deleteFoodAndTagsByFoodId(foodId: Long): Int

    suspend fun deleteFoodAndTag(foodId: Long, tagId: Long): Int

    suspend fun deleteFoodById(id: Long): Int

    //tag
    fun getFoodTagFlow(): Flow<List<FoodTag>>

    suspend fun getTagById(id: Long): FoodTag

    suspend fun saveOrModifyFoodTag(tag: FoodTag): Long

    suspend fun deleteFoodTag(id: Long): Int
}