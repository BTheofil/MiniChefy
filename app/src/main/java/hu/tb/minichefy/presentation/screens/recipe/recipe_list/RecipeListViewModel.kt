package hu.tb.minichefy.presentation.screens.recipe.recipe_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.data.repository.RecipeDatabaseRepositoryImpl
import hu.tb.minichefy.domain.model.Recipe
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val repository: RecipeDatabaseRepositoryImpl
) : ViewModel() {

    data class UiState(
        val recipeList: List<Recipe> = emptyList(),
        var selectedRecipeId: Long? = null,
        var showSettingsPanel: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    sealed class UiEvent {
        data class OnItemClick(val recipeId: Long) : UiEvent()
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    sealed class OnEvent {
        data class OnItemClick(val recipeId: Long) : OnEvent()
        data class OpenRecipeSettingsPanel(val recipeId: Long): OnEvent()
        data object DeleteItem : OnEvent()
    }

    init {
        viewModelScope.launch {
            repository.getAllRecipe().collect { data ->
                _uiState.update {
                    it.copy(recipeList = data)
                }
            }
        }
    }

    fun onEvent(event: OnEvent) {
        when (event) {
            is OnEvent.OnItemClick -> viewModelScope.launch {
                _uiEvent.send(UiEvent.OnItemClick(event.recipeId))
            }

            OnEvent.DeleteItem -> viewModelScope.launch {
                repository.deleteRecipe(uiState.value.selectedRecipeId!!)
            }

            is OnEvent.OpenRecipeSettingsPanel -> {
                _uiState.update {
                    it.copy(
                        selectedRecipeId = event.recipeId,
                        showSettingsPanel = true
                    )
                }
            }
        }
    }
}