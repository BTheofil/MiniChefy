package hu.tb.minichefy.domain.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val quantity: Int,
    val howToSteps: ArrayList<RecipeStepEntity>
)
