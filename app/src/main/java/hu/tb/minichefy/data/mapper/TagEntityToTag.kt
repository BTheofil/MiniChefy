package hu.tb.minichefy.data.mapper

import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.entity.TagEntity

class TagEntityToTag {

    fun map(entityTag: TagEntity) =
        FoodTag(entityTag.tagId, entityTag.tag)

}