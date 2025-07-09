package hu.tb.minichefy.data.repository

import hu.tb.minichefy.data.data_source.dao.StorageDAO
import hu.tb.minichefy.data.mapper.FoodEntityToFood
import hu.tb.minichefy.data.mapper.SimpleFoodEntityToFoodSummery
import hu.tb.minichefy.data.mapper.TagEntityToTag
import hu.tb.minichefy.di.DISH_TAG_ID
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
        val entities = dao.getAllFoodEntity()
        return entities.map {
            it.map { foodWithTags ->
                FoodEntityToFood().map(foodWithTags)
            }
        }
    }

    override fun getStorageIngredients(): Flow<List<FoodSummary>> {
        val entities = dao.getSimpleFoodExcludedTag(DISH_TAG_ID.toLong())
        return entities.map { simpleFoodEntities ->
            simpleFoodEntities.map {
                SimpleFoodEntityToFoodSummery().map(it)
            }.sortedBy { it.title }
        }
    }

    override fun searchFoodByTitle(title: String): Flow<List<Food>> {
        val entities = dao.searchFoodByTitle(title)
        return entities.map { foodWithTags ->
            foodWithTags.map {
                FoodEntityToFood().map(it)
            }
        }
    }

    override fun searchIngredientsByLikelyTitle(searchText: String): Flow<List<FoodSummary>> {
        val entities = dao.searchSimpleFoodsByTitle("%$searchText%", DISH_TAG_ID.toLong())
        return entities.map { simpleFoodEntities ->
            simpleFoodEntities.map {
                SimpleFoodEntityToFoodSummery().map(it)
            }.sortedBy { it.title }
        }
    }

    override fun searchFoodsByTag(tagIds: List<Long>): Flow<List<Food>> {
        val foodEntity = dao.searchFoodsByTag(tagIds)
        return foodEntity.map { foodWithTags ->
            foodWithTags.map {
                FoodEntityToFood().map(it)
            }
        }
    }

    override suspend fun saveOrModifyFood(
        id: Long?,
        title: String,
        icon: String,
        quantity: Float,
        unitOfMeasurement: UnitOfMeasurement
    ): Long {
        val temp = FoodEntity(
            foodId = id,
            title = title,
            image = icon,
            quantity = quantity,
            unitOfMeasurement = unitOfMeasurement
        )

        return dao.insertFoodEntity(temp)
    }

    override suspend fun saveFoodAndTagConnection(foodId: Long, tagId: Long): Long =
        dao.insertFoodTagCrossRef(FoodAndTagsCrossRef(foodId, tagId))

    override suspend fun deleteFoodAndTagsByFoodId(foodId: Long) =
        dao.deleteFoodWithTagsConnectionByFoodId(foodId)

    override suspend fun deleteFoodAndTag(foodId: Long, tagId: Long) =
        dao.deleteFoodTagCrossRef(foodId, tagId)

    override suspend fun deleteFoodById(id: Long) =
        dao.deleteFoodEntity(id)

    //tag
    override fun getFoodTagFlow(): Flow<List<FoodTag>> {
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

    override suspend fun deleteFoodTag(id: Long) = dao.deleteFoodTag(id)
}


