package hu.tb.minichefy.data.data_source.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.tb.minichefy.data.data_source.dao.RecipeDAO
import hu.tb.minichefy.data.data_source.dao.StorageDAO
import hu.tb.minichefy.domain.model.entity_extentsion.IdListConverter
import hu.tb.minichefy.domain.model.recipe.entity.RecipeEntity
import hu.tb.minichefy.domain.model.recipe.entity.RecipeFoodCrossRef
import hu.tb.minichefy.domain.model.recipe.entity.RecipeStepEntity
import hu.tb.minichefy.domain.model.recipe.entity.TimeUnitConverter
import hu.tb.minichefy.domain.model.storage.entity.FoodAndTagsCrossRef
import hu.tb.minichefy.domain.model.storage.entity.FoodEntity
import hu.tb.minichefy.domain.model.storage.entity.TagEntity
import hu.tb.minichefy.domain.model.storage.entity.UnitOfMeasurementConverter

@Database(
    entities = [
        RecipeEntity::class,
        RecipeStepEntity::class,
        FoodEntity::class,
        TagEntity::class,
        RecipeFoodCrossRef::class,
        FoodAndTagsCrossRef::class
    ], version = 1, exportSchema = false
)
@TypeConverters(
    UnitOfMeasurementConverter::class,
    TimeUnitConverter::class,
    IdListConverter::class
)
abstract class MiniChefyDatabase : RoomDatabase() {

    abstract val recipeDao: RecipeDAO
    abstract val storageDao: StorageDAO

    companion object {
        const val DATABASE_NAME = "mini_chefy_database"
    }
}