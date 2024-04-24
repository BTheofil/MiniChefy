package hu.tb.minichefy.domain.model.storage.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement

@Entity
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    val foodId: Long?,
    val icon: Int,
    val title: String,
    val quantity: Float,
    val unitOfMeasurement: UnitOfMeasurement
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