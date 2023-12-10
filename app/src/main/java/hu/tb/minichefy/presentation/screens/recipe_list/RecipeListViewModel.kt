package hu.tb.minichefy.presentation.screens.recipe_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.data.repository.RecipeDatabaseRepositoryImpl
import hu.tb.minichefy.domain.model.Recipe
import hu.tb.minichefy.domain.model.RecipeStep
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val repository: RecipeDatabaseRepositoryImpl
) : ViewModel() {

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
        viewModelScope.launch {
            repository.getAllRecipe().collect { data ->
                _state.value = _state.value.copy(
                    recipeList = data
                )
                data.forEach {
                    it.howToSteps.forEach {
                        Log.d("MYTAG",it.step)

                    }
                }
            }
        }
    }

    fun onItemClick(recipeId: Int) {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.OnItemClick(recipeId))
        }
    }
}