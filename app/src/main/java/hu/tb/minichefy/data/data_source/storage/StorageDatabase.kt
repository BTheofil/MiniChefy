package hu.tb.minichefy.data.data_source.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.tb.minichefy.domain.model.storage.entity.StorageFoodEntity
import hu.tb.minichefy.domain.model.storage.entity.UnitOfMeasurementConverter

@Database(entities = [StorageFoodEntity::class], version = 1, exportSchema = false)
@TypeConverters(UnitOfMeasurementConverter::class)
abstract class StorageDatabase: RoomDatabase() {

    abstract val storageDao: StorageDAO

    companion object {
        const val DATABASE_NAME = "storage_database"
    }
}