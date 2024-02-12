package hu.tb.minichefy.data.data_source.recipe

import androidx.room.Database
import androidx.room.RoomDatabase
import hu.tb.minichefy.domain.model.recipe.entity.RecipeEntity
import hu.tb.minichefy.domain.model.recipe.entity.RecipeStepEntity

@Database(entities = [RecipeEntity::class, RecipeStepEntity::class], version = 1, exportSchema = false)
abstract class RecipeDatabase: RoomDatabase() {

    abstract val recipeDao: RecipeDAO

    companion object {
        const val DATABASE_NAME = "recipe_database"
    }
}