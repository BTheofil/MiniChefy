package hu.tb.minichefy.domain.model.recipe.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import hu.tb.minichefy.domain.model.recipe.TimeUnit
import hu.tb.minichefy.domain.model.storage.entity.FoodEntity

@Entity
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val recipeId: Long?,
    val icon: Int,
    val title: String,
    val quantity: Int,
    val timeToCreate: Int,
    val timeUnit: TimeUnit,
    val ingredientList: FoodEntityWrapper
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = arrayOf("recipeId"),
            childColumns = arrayOf("recipeEntityId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RecipeStepEntity(
    @PrimaryKey(autoGenerate = true)
    val recipeStepId: Long?,
    val recipeEntityId: Long,
    val step: String
)

data class RecipeWithSteps(
    @Embedded
    val recipeEntity: RecipeEntity,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "recipeEntityId"
    )
    val recipeSteps: List<RecipeStepEntity>
)

data class FoodEntityWrapper(
    val ingredients: List<FoodEntity>
)

