package hu.tb.minichefy.domain.repository

import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodSummary
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import kotlinx.coroutines.flow.Flow

interface StorageRepository {

    //food
    fun getAllFood(): Flow<List<Food>>

    fun getStorageIngredients(): Flow<List<FoodSummary>>

    fun searchFoodByTitle(title: String): Flow<List<Food>>

    fun searchIngredientsByLikelyTitle(searchText: String): Flow<List<FoodSummary>>

    fun searchFoodsByTag(tagIds: List<Long>): Flow<List<Food>>

    suspend fun saveOrModifyFood(
        id: Long? = null,
        title: String,
        icon: String,
        quantity: Float,
        unitOfMeasurement: UnitOfMeasurement
    ): Long

    suspend fun saveFoodAndTagConnection(foodId: Long, tagId: Long): Long

    suspend fun deleteFoodAndTagsByFoodId(foodId: Long)

    suspend fun deleteFoodAndTag(foodId: Long, tagId: Long)

    suspend fun deleteFoodById(id: Long)

    //tag
    fun getFoodTagFlow(): Flow<List<FoodTag>>

    suspend fun getTagById(id: Long): FoodTag

    suspend fun saveOrModifyFoodTag(tag: FoodTag): Long

    suspend fun deleteFoodTag(id: Long)
}