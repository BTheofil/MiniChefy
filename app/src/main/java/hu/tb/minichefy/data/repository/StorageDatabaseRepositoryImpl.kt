package hu.tb.minichefy.data.repository

import hu.tb.minichefy.data.data_source.storage.StorageDAO
import hu.tb.minichefy.data.mapper.FoodEntityToFood
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.toFoodEntity
import hu.tb.minichefy.domain.model.storage.toFoodTagEntity
import hu.tb.minichefy.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StorageDatabaseRepositoryImpl @Inject constructor(
    private val dao: StorageDAO
) : StorageRepository {

    //food
    override fun getAllFoodEntity(): Flow<List<Food>> {
        val foodsEntities = dao.getAllStorageFood()
        return foodsEntities.map { storageFoodEntities ->
            storageFoodEntities.map {
                FoodEntityToFood().map(it)
            }
        }
    }

    override suspend fun saveOrModifyFoodEntity(food: Food): Long =
        dao.saveOrModifyFood(food.toFoodEntity())

    override suspend fun deleteFoodEntity(id: Long): Int =
        dao.deleteFoodEntity(id)

    //tag
    override fun getAllFoodTagEntity(): Flow<List<FoodTag>> {
        val tagEntities = dao.getAllFoodTag()
        return tagEntities.map { entities ->
            entities.map {
                FoodTag(
                    id = it.id,
                    tag = it.tag
                )
            }
        }
    }

    override suspend fun saveOrModifyFoodTagEntity(tag: FoodTag): Long =
        dao.saveOrModifyFoodTag(tag.toFoodTagEntity())

    override suspend fun deleteFoodTagEntity(id: Long): Int = dao.deleteFoodTag(id)
}