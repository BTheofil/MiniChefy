package hu.tb.minichefy.data.data_source

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import hu.tb.minichefy.data.data_source.dao.RecipeDAO
import hu.tb.minichefy.data.data_source.db.MiniChefyDatabase
import hu.tb.minichefy.domain.model.recipe.TimeUnit
import hu.tb.minichefy.domain.model.recipe.entity.RecipeEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MiniChefyDatabaseTest {

    private lateinit var recipeDao: RecipeDAO
    private lateinit var miniChefyDatabase: MiniChefyDatabase

    @Before
    fun createDb() {
        miniChefyDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), MiniChefyDatabase::class.java
        )
            .build()
        recipeDao = miniChefyDatabase.recipeDao
    }

    @After
    fun closeDb() {
        miniChefyDatabase.close()
    }

    @Test
    fun insertAndRetrieveRecipe() = runBlocking {
        val recipeEntity = RecipeEntity(recipeId = null, icon = 1, title = "Pasta", quantity = 2, timeUnit = TimeUnit.MINUTES, timeToCreate = 30f)

        val recipeId = recipeDao.insertRecipe(recipeEntity)

        val recipeWithSteps = recipeDao.getRecipeById(recipeId)
        assertEquals("Pasta", recipeWithSteps.recipeEntity.title)
    }
}