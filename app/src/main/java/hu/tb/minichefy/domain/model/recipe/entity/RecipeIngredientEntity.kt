package hu.tb.minichefy.domain.model.recipe.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement

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
data class RecipeIngredientEntity(
    @PrimaryKey(autoGenerate = true)
    val ingredientId: Long?,
    @ColumnInfo(index = true)
    val recipeEntityId: Long,
    val title: String,
    val quantity: Float,
    val unitOfMeasurement: UnitOfMeasurement
)