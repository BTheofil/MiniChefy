package hu.tb.minichefy.data.repository

import hu.tb.minichefy.data.data_source.dao.StorageDAO
import hu.tb.minichefy.data.mapper.FoodEntityToFood
import hu.tb.minichefy.data.mapper.ProductTagEntityToTag
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodSummary
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.domain.model.storage.entity.FoodEntity
import hu.tb.minichefy.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StorageDatabaseRepositoryImpl @Inject constructor(
    private val dao: StorageDAO
) : StorageRepository {

    //food
    override fun getAllFood(): Flow<List<Food>> {
        val foodsEntities = dao.getAllStorageFood()
        return foodsEntities.map { storageFoodEntities ->
            storageFoodEntities.map {
                FoodEntityToFood().map(it)
            }
        }
    }

    override suspend fun getAllStorageFoodName(): List<FoodSummary> {
        val entities = dao.getAllStorageFoodName()
        return entities.map {
            FoodSummary(
                id = it.id,
                title = it.title,
            )
        }.sortedBy { it.title }
    }

    override suspend fun saveOrModifyFood(food: Food): Long =
        dao.saveOrModifyFood(food.toFoodEntity())

    override suspend fun searchFoodByDishProperties(
        title: String,
        uof: UnitOfMeasurement
    ): Food? {
        val entity: FoodEntity = dao.searchFoodByDishProperties(title, unitOfMeasurement = uof)
            ?: return null
        return FoodEntityToFood().map(entity)
    }

    override suspend fun searchProductByTitle(searchText: String): List<FoodSummary> {
        val products = dao.searchProductByTitle("%$searchText%")
        return products.map {
            FoodSummary(
                id = it.id,
                title = it.title,
            )
        }.sortedBy { it.title }
    }

    override suspend fun deleteFoodById(id: Long): Int =
        dao.deleteFoodEntity(id)

    //tag
    override fun getAllFoodTag(): Flow<List<FoodTag>> {
        val tagEntities = dao.getAllFoodTag()
        return tagEntities.map { entities ->
            entities.map {
                ProductTagEntityToTag().map(it)
            }
        }
    }

    override suspend fun getTagById(id: Long): FoodTag {
        val tagEntity = dao.getTagById(id)
        return ProductTagEntityToTag().map(tagEntity)
    }

    override suspend fun saveOrModifyFoodTag(tag: FoodTag): Long =
        dao.saveOrModifyFoodTag(tag.toFoodTagEntity())

    override suspend fun deleteFoodTag(id: Long): Int = dao.deleteFoodTag(id)
}


