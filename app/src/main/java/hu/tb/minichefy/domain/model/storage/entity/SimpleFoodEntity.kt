package hu.tb.minichefy.domain.model.storage.entity

import androidx.room.Entity

@Entity
data class SimplerFoodEntity(
    val foodId: Long,
    val title: String,
)