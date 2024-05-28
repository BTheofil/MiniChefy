package hu.tb.minichefy.domain.model.recipe.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import hu.tb.minichefy.domain.model.recipe.TimeUnit

@Entity
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val recipeId: Long?,
    val image: String,
    val title: String,
    val quantity: Int,
    val timeToCreate: Int,
    val timeUnit: TimeUnit,
)

data class RecipeBlock(
    @Embedded
    val recipeEntity: RecipeEntity,

    @Relation(
        entity = RecipeStepEntity::class,
        parentColumn = "recipeId",
        entityColumn = "recipeEntityId"
    )
    val stepList: List<RecipeStepEntity>,

    @Relation(
        entity = RecipeIngredientEntity::class,
        parentColumn = "recipeId",
        entityColumn = "recipeEntityId",
    )
    val ingredientList: List<RecipeIngredientEntity>
)