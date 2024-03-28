package hu.tb.minichefy.domain.model.recipe.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import hu.tb.minichefy.domain.model.recipe.TimeUnit
import hu.tb.minichefy.domain.model.storage.entity.FoodEntity
import hu.tb.minichefy.domain.model.storage.entity.FoodWithTags

@Entity
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val recipeId: Long?,
    val icon: Int,
    val title: String,
    val quantity: Int,
    val timeToCreate: Int,
    val timeUnit: TimeUnit,
)

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

// connection between recipe & food many to many
@Entity(primaryKeys = ["recipeId", "foodId"])
data class RecipeFoodCrossRef(
    val recipeId: Long,
    @ColumnInfo(index = true)
    val foodId: Long
)

data class RecipeBlock(
    @Embedded
    val recipeEntity: RecipeEntity,

    @Relation(
        entity = RecipeStepEntity::class,
        parentColumn = "recipeId",
        entityColumn = "recipeEntityId"
    )
    val recipeSteps: List<RecipeStepEntity>,

    @Relation(
        entity = FoodEntity::class,
        parentColumn = "recipeId",
        entityColumn = "foodId",
        associateBy = Junction(RecipeFoodCrossRef::class)
    )
    val ingredients: List<FoodWithTags>
)

