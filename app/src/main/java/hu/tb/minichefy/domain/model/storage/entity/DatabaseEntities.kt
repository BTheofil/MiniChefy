package hu.tb.minichefy.domain.model.storage.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement

const val UNKNOWN_TAG_ID = 4
const val DISH_TAG_ID = 3

@Entity
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    val foodId: Long?,
    val icon: Int,
    val title: String,
    val quantity: Float,
    val unitOfMeasurement: UnitOfMeasurement
)

@Entity
data class SimplerFoodEntity(
    val foodId: Long,
    val title: String,
)

@Entity
data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    val tagId: Long?,
    val tag: String
)

@Entity(primaryKeys = ["foodId", "tagId"])
data class FoodAndTagsCrossRef(
    val foodId: Long,
    @ColumnInfo(index = true)
    val tagId: Long
)

//many to many food & tags
data class FoodWithTags(
    @Embedded val foodEntity: FoodEntity,
    @Relation(
        parentColumn = "foodId",
        entityColumn = "tagId",
        associateBy = Junction(FoodAndTagsCrossRef::class)
    )
    val tags: List<TagEntity>
)

