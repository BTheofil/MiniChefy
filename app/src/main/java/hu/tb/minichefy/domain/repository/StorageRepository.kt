package hu.tb.minichefy.domain.repository

import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodTag
import kotlinx.coroutines.flow.Flow

interface StorageRepository {

    //food
    fun getAllFood(): Flow<List<Food>>

    suspend fun saveOrModifyFood(food: Food): Long

    suspend fun deleteFoodById(id: Long): Int

    //tag
    fun getAllFoodTag(): Flow<List<FoodTag>>

    suspend fun saveOrModifyFoodTag(tag: FoodTag): Long

    suspend fun deleteFoodTag(id: Long): Int
}