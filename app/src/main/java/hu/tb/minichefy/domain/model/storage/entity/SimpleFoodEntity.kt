package hu.tb.minichefy.domain.model.storage.entity

import androidx.room.Entity

@Entity
data class SimpleFoodEntity(
    val foodId: Long,
    val title: String,
)