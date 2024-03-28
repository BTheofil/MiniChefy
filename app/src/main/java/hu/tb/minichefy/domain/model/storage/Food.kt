package hu.tb.minichefy.domain.model.storage

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

enum class UnitOfMeasurement(val id: Int) {
    PIECE(0),
    DL(1),
    L(2),
    G(3),
    DKG(4),
    KG(5)
}
