package hu.tb.minichefy.domain.model.storage

import hu.tb.minichefy.R

data class FoodSummary(
    val id: Long,
    val title: String
)

data class Food(
    val id: Long? = null,
    val icon: Int,
    val title: String,
    val quantity: Float,
    val unitOfMeasurement: UnitOfMeasurement,
    val foodTagList: List<FoodTag>?
)

enum class UnitOfMeasurement(val stringResource: Int) {
    PIECE(R.string.piece),
    DL(R.string.dl),
    L(R.string.l),
    G(R.string.g),
    DKG(R.string.dkg),
    KG(R.string.kg)
}

enum class FoodType(val stringResource: Int) {
    LIQUID(R.string.liquid),
    SOLID(R.string.solid),
    PIECE(R.string.piece)
}
