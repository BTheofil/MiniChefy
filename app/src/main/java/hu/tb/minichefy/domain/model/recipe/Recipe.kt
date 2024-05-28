package hu.tb.minichefy.domain.model.recipe

import hu.tb.minichefy.R
import hu.tb.minichefy.domain.model.IconResource

data class Recipe(
    val id: Long? = null,
    val icon: IconResource,
    val title: String,
    val quantity: Int,
    val timeToCreate: Int,
    val timeUnit: TimeUnit,
    val howToSteps: List<RecipeStep>,
    val ingredientList: List<RecipeIngredient>
)

enum class TimeUnit(val id: Int, val stringResource: Int) {
    MINUTES(id = 1, stringResource = R.string.minute),
    HOUR(id = 2, stringResource = R.string.hour)
}


