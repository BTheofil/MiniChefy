package hu.tb.minichefy.domain.model.recipe.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val recipeId: Long?,
    val icon: Int,
    val title: String,
    val quantity: Int,
)

@Entity
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


