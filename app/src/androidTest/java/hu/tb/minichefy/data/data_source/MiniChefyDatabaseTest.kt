package hu.tb.minichefy.data.data_source

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import hu.tb.minichefy.data.data_source.dao.RecipeDAO
import hu.tb.minichefy.data.data_source.dao.StorageDAO
import hu.tb.minichefy.data.data_source.db.MiniChefyDatabase
import hu.tb.minichefy.domain.model.recipe.TimeUnit
import hu.tb.minichefy.domain.model.recipe.entity.RecipeEntity
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.domain.model.storage.entity.DISH_TAG_ID
import hu.tb.minichefy.domain.model.storage.entity.FoodAndTagsCrossRef
import hu.tb.minichefy.domain.model.storage.entity.FoodEntity
import hu.tb.minichefy.domain.model.storage.entity.TagEntity
import hu.tb.minichefy.domain.model.storage.entity.UNKNOWN_TAG_ID
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MiniChefyDatabaseTest {

    private lateinit var recipeDao: RecipeDAO
    private lateinit var storageDao: StorageDAO
    private lateinit var miniChefyDatabase: MiniChefyDatabase

    @Before
    fun createDb() {
        miniChefyDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), MiniChefyDatabase::class.java
        )
            .build()
        recipeDao = miniChefyDatabase.recipeDao
        storageDao = miniChefyDatabase.storageDao

        val tagList = listOf(
            TagEntity(tagId = 1, tag = "fruit"),
            TagEntity(tagId = 2, tag = "vegetable"),
            TagEntity(tagId = DISH_TAG_ID.toLong(), tag = "dish"),
            TagEntity(tagId = UNKNOWN_TAG_ID.toLong(), tag = "unknown"),
        )
        runBlocking {
            tagList.forEach {
                storageDao.insertTagEntity(it)
            }
        }
    }

    @After
    fun closeDb() {
        miniChefyDatabase.close()
    }

    @Test
    fun insertAndRetrieveRecipe() = runBlocking {
        val recipeEntity = RecipeEntity(
            recipeId = null,
            icon = 1,
            title = "Pasta",
            quantity = 2,
            timeUnit = TimeUnit.MINUTES,
            timeToCreate = 30,
        )

        val recipeId = recipeDao.insertRecipeEntity(recipeEntity)

        val recipeWithSteps = recipeDao.getRecipeById(recipeId)
        assertEquals("Pasta", recipeWithSteps.recipeEntity.title)
    }

    @Test
    fun insertAndRetrieveFood() = runBlocking {
        val foodEntity = FoodEntity(
            foodId = 1,
            icon = 2,
            title = "Tomato",
            quantity = 1f,
            unitOfMeasurement = UnitOfMeasurement.KG
        )

        storageDao.insertFoodEntity(foodEntity)

        val foodWithTags = storageDao.searchFoodByTitle("Tomato")

        //return food
        foodWithTags.also {
            assertEquals(2, it!!.foodEntity.icon)
            assertEquals("Tomato", it.foodEntity.title)
            assertEquals(1f, it.foodEntity.quantity)
            assertEquals(UnitOfMeasurement.KG, it.foodEntity.unitOfMeasurement)
        }

        //delete food
        storageDao.deleteFoodEntity(id = 1)
        val result = storageDao.searchFoodByTitle("Tomato")
        assertEquals(null, result)

        //return food with tag
        storageDao.insertFoodEntity(foodEntity)
        storageDao.insertFoodTagCrossRef(
            FoodAndTagsCrossRef(
                foodId = foodEntity.foodId!!,
                tagId = 1
            )
        )
        val result2 = storageDao.getKnownFoodsList()
        assertEquals(1, result2[0].tags.size)
    }
}