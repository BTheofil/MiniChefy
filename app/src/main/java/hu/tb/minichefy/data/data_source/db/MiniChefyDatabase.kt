package hu.tb.minichefy.data.data_source.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.tb.minichefy.data.data_source.dao.RecipeDAO
import hu.tb.minichefy.data.data_source.dao.StorageDAO
import hu.tb.minichefy.domain.model.recipe.entity.RecipeEntity
import hu.tb.minichefy.domain.model.recipe.entity.RecipeStepEntity
import hu.tb.minichefy.domain.model.recipe.entity.TimeUnitConverter
import hu.tb.minichefy.domain.model.storage.entity.FoodEntity
import hu.tb.minichefy.domain.model.storage.entity.FoodTagEntity
import hu.tb.minichefy.domain.model.storage.entity.RoomTypeConverters
import hu.tb.minichefy.domain.model.storage.entity.UnitOfMeasurementConverter

@Database(
    entities = [
        RecipeEntity::class,
        RecipeStepEntity::class,
        FoodEntity::class,
        FoodTagEntity::class,
    ], version = 1, exportSchema = false
)
@TypeConverters(
    UnitOfMeasurementConverter::class,
    TimeUnitConverter::class,
    RoomTypeConverters::class
)
abstract class MiniChefyDatabase : RoomDatabase() {

    abstract val recipeDao: RecipeDAO
    abstract val storageDao: StorageDAO

    companion object {
        const val DATABASE_NAME = "mini_chefy_database"
    }
}