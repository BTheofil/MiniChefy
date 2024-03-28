package hu.tb.minichefy.data.repository

import hu.tb.minichefy.data.data_source.dao.StorageDAO
import hu.tb.minichefy.data.mapper.FoodEntityToFood
import hu.tb.minichefy.data.mapper.TagEntityToTag
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodSummary
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.domain.model.storage.entity.FoodAndTagsCrossRef
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
                id = it.foodId,
                title = it.title,
            )
        }.sortedBy { it.title }
    }

    override suspend fun searchFoodByTitle(title: String): Food? {
        val entity = dao.searchFoodByTitle(title) ?: return null
        return FoodEntityToFood().map(entity)
    }

    override suspend fun searchProductByTitle(searchText: String): List<FoodSummary> {
        val products = dao.searchProductByTitle("%$searchText%")
        return products.map {
            FoodSummary(
                id = it.foodId,
                title = it.title,
            )
        }.sortedBy { it.title }
    }

    override suspend fun saveOrModifyFood(
        id: Long?,
        title: String,
        icon: Int,
        quantity: Float,
        unitOfMeasurement: UnitOfMeasurement
    ): Long {
        val temp = FoodEntity(
            foodId = id,
            title = title,
            icon = icon,
            quantity = quantity,
            unitOfMeasurement = unitOfMeasurement
        )

        return dao.insertFoodEntity(temp)
    }

    override suspend fun saveFoodAndTag(foodId: Long, tagId: Long): Long =
        dao.insertFoodTagCrossRef(FoodAndTagsCrossRef(foodId, tagId))

    override suspend fun deleteFoodAndTag(foodId: Long, tagId: Long): Int =
        dao.deleteFoodTagCrossRef(foodId, tagId)

    override suspend fun deleteFoodById(id: Long): Int =
        dao.deleteFoodEntity(id)

    //tag
    override fun getAllFoodTag(): Flow<List<FoodTag>> {
        val tagEntities = dao.getAllFoodTagFlow()
        return tagEntities.map { entities ->
            entities.map {
                TagEntityToTag().map(it)
            }
        }
    }

    override suspend fun getTagById(id: Long): FoodTag {
        val tagEntity = dao.getTagById(id)
        return TagEntityToTag().map(tagEntity)
    }

    override suspend fun saveOrModifyFoodTag(tag: FoodTag): Long =
        dao.insertTagEntity(tag.toFoodTagEntity())

    override suspend fun deleteFoodTag(id: Long): Int = dao.deleteFoodTag(id)
}


