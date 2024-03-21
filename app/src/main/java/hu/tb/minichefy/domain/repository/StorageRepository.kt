package hu.tb.minichefy.domain.repository

import hu.tb.minichefy.domain.model.recipe.Ingredient
import hu.tb.minichefy.domain.model.recipe.IngredientBase
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import kotlinx.coroutines.flow.Flow

interface StorageRepository {

    //food
    fun getAllFood(): Flow<List<Food>>

    suspend fun getAllStorageFoodName(): List<IngredientBase>

    suspend fun saveOrModifyFood(food: Food): Long

    suspend fun searchFoodByDishProperties(title: String, uof: UnitOfMeasurement): Food?

    suspend fun searchProductByTitle(searchText: String): List<Ingredient>

    suspend fun deleteFoodById(id: Long): Int

    //tag
    fun getAllFoodTag(): Flow<List<FoodTag>>

    suspend fun getTagById(id: Long): FoodTag

    suspend fun saveOrModifyFoodTag(tag: FoodTag): Long

    suspend fun deleteFoodTag(id: Long): Int
}