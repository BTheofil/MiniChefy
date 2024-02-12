package hu.tb.minichefy.data.data_source.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hu.tb.minichefy.domain.model.storage.entity.StorageFoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StorageDAO {

    @Query("SELECT * FROM StorageFoodEntity")
    fun getAllStorageFood(): Flow<List<StorageFoodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrModifyFood(foodEntity: StorageFoodEntity): Long

    @Query("DELETE FROM StorageFoodEntity WHERE id = :id")
    suspend fun deleteFoodEntity(id: Long): Int
}