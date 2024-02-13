package hu.tb.minichefy.domain.model.storage

data class Food(
    val id: Long? = null,
    val title: String,
    val quantity: Int,
    val unitOfMeasurement: UnitOfMeasurement,
    val type: String
)

enum class UnitOfMeasurement(val id: Int){
    DL(1),
    L(2),
    G(3),
    DKG(4),
    KG(5),
}
