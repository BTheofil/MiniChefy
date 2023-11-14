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
                Recipe(0, "alma", 2, listOf(RecipeStep(0, "one"), RecipeStep(1, "two"))),
                Recipe(1, "banan", 5, listOf(RecipeStep(0, "one"))),
                Recipe(2, "citrom", 3, listOf(RecipeStep(0, "one"))),
                Recipe(3, "dio", 1, listOf(RecipeStep(0, "one")))
            )
        )
    }
}