package hu.tb.minichefy.domain.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeStepEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val step: String
)
