package hu.tb.minichefy.data.data_source

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import hu.tb.minichefy.data.data_source.recipe.RecipeDAO
import hu.tb.minichefy.data.data_source.recipe.RecipeDatabase
import hu.tb.minichefy.domain.model.recipe.entity.RecipeEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeDatabaseTest {

    private lateinit var recipeDao: RecipeDAO
    private lateinit var recipeDatabase: RecipeDatabase

    @Before
    fun createDb() {
        recipeDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), RecipeDatabase::class.java
        )
            .build()
        recipeDao = recipeDatabase.recipeDao
    }

    @After
    fun closeDb() {
        recipeDatabase.close()
    }

    @Test
    fun insertAndRetrieveRecipe() = runBlocking {
        val recipeEntity = RecipeEntity(recipeId = null, icon = 1, title = "Pasta", quantity = 2)

        val recipeId = recipeDao.insertRecipe(recipeEntity)

        val recipeWithSteps = recipeDao.getRecipeById(recipeId)
        assertEquals("Pasta", recipeWithSteps.recipeEntity.title)
    }
}