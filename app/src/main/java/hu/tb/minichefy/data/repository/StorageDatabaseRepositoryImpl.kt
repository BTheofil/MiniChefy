package hu.tb.minichefy.data.repository

import hu.tb.minichefy.data.data_source.storage.StorageDAO
import hu.tb.minichefy.data.mapper.FoodEntityToFood
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.entity.StorageFoodEntity
import hu.tb.minichefy.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StorageDatabaseRepositoryImpl @Inject constructor(
    private val dao: StorageDAO
) : StorageRepository {

    override fun getAllFoodEntity(): Flow<List<Food>> {
        val foodsEntities= dao.getAllStorageFood()
        return foodsEntities.map { storageFoodEntities ->
            storageFoodEntities.map {
                FoodEntityToFood().map(it)
            }
        }
    }

    override suspend fun saveOrModifyFoodEntity(foodEntity: StorageFoodEntity): Long =
       dao.saveOrModifyFood(foodEntity)

    override suspend fun deleteFoodEntity(id: Long): Int =
        dao.deleteFoodEntity(id)

}