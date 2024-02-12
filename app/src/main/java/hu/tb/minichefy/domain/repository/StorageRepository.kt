package hu.tb.minichefy.domain.repository

import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.entity.StorageFoodEntity
import kotlinx.coroutines.flow.Flow

interface StorageRepository {

    fun getAllFoodEntity(): Flow<List<Food>>

    suspend fun saveOrModifyFoodEntity(foodEntity: StorageFoodEntity): Long

    suspend fun deleteFoodEntity(id: Long): Int
}