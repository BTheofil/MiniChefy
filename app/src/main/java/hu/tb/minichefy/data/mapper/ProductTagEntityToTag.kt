package hu.tb.minichefy.data.mapper

import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.entity.FoodTagEntity

class ProductTagEntityToTag {

    fun map(entityTag: FoodTagEntity) =
        FoodTag(entityTag.id, entityTag.tag)

}