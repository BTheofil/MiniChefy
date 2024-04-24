package hu.tb.minichefy.domain.model.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    val tagId: Long?,
    val tag: String
)