package hu.tb.minichefy.domain.model.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val recipeId: Int?,
    val title: String,
    val quantity: Int,
)

@Entity
data class RecipeStepEntity(
    @PrimaryKey(autoGenerate = true)
    val recipeStepId: Int?,
    val recipeEntityId: Int,
    val step: String
)

data class RecipeHowToCreateList(
    @Embedded val recipeEntity: RecipeEntity,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "recipeStepId"
    )
    val howToStepsList: List<RecipeStepEntity>
)


