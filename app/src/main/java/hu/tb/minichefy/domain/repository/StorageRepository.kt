package hu.tb.minichefy.domain.repository

import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodTag
import kotlinx.coroutines.flow.Flow

interface StorageRepository {

    //food
    fun getAllFoodEntity(): Flow<List<Food>>

    suspend fun saveOrModifyFoodEntity(food: Food): Long

    suspend fun deleteFoodEntity(id: Long): Int

    //tag
    fun getAllFoodTagEntity(): Flow<List<FoodTag>>

    suspend fun saveOrModifyFoodTagEntity(tag: FoodTag): Long

    suspend fun deleteFoodTagEntity(id: Long): Int
}