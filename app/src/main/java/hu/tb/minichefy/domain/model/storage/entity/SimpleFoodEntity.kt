package hu.tb.minichefy.domain.model.storage.entity

import androidx.room.Entity
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement

@Entity
data class SimpleFoodEntity(
    val foodId: Long,
    val title: String,
    val unitOfMeasurement: UnitOfMeasurement
)