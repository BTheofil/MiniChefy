package hu.tb.minichefy.domain.model.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement

@Entity
data class StorageFoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val title: String,
    val quantity: Int,
    val unitOfMeasurement: UnitOfMeasurement
)