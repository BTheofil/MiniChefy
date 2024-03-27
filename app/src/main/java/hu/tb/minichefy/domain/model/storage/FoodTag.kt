package hu.tb.minichefy.domain.model.storage

import hu.tb.minichefy.domain.model.storage.entity.TagEntity

data class FoodTag(
    val id: Long? = null,
    val tag: String
) {
    fun toFoodTagEntity() =
        TagEntity(
            tagId = id,
            tag = tag
        )
}

