package hu.tb.minichefy.domain.model.recipe.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

//one to many recipe & step
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["recipeId"],
            childColumns = ["recipeEntityId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RecipeStepEntity(
    @PrimaryKey(autoGenerate = true)
    val recipeStepId: Long?,
    @ColumnInfo(index = true)
    val recipeEntityId: Long,
    val step: String
)