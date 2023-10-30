package hu.tb.minichefy.recipe_list

import androidx.lifecycle.ViewModel
import hu.tb.minichefy.domain.model.Recipe
import hu.tb.minichefy.domain.model.RecipeStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipeListViewModel: ViewModel() {

    data class State(
        val recipeList: List<Recipe> = emptyList()
    )

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    init {
        _state.value = _state.value.copy(
            recipeList = listOf(
                Recipe(0, RecipeStep(0, "alma")),
                Recipe(1, RecipeStep(1, "banan")),
                Recipe(2, RecipeStep(2, "cekla"))
            )
        )
    }
}