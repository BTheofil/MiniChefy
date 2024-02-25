package hu.tb.minichefy.data.data_source.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.tb.minichefy.domain.model.storage.entity.FoodEntity
import hu.tb.minichefy.domain.model.storage.entity.FoodTagEntity
import hu.tb.minichefy.domain.model.storage.entity.RoomTypeConverters
import hu.tb.minichefy.domain.model.storage.entity.UnitOfMeasurementConverter

@Database(entities = [FoodEntity::class, FoodTagEntity::class], version = 1, exportSchema = false)
@TypeConverters(UnitOfMeasurementConverter::class, RoomTypeConverters::class)
abstract class StorageDatabase: RoomDatabase() {

    abstract val storageDao: StorageDAO

    companion object {
        const val DATABASE_NAME = "storage_database"
    }
}