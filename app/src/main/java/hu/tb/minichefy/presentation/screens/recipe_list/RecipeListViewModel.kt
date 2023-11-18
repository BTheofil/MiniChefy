package hu.tb.minichefy.presentation.screens.recipe_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.tb.minichefy.domain.model.Recipe
import hu.tb.minichefy.domain.model.RecipeStep
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RecipeListViewModel: ViewModel() {

    data class State(
        val recipeList: List<Recipe> = emptyList()
    )

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    sealed class UiEvent {
        data class OnItemClick(val recipeId: Int) : UiEvent()
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

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

    fun onItemClick(recipeId: Int){
        viewModelScope.launch {
            _uiEvent.send(UiEvent.OnItemClick(recipeId))
        }
    }
}