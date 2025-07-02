package hu.tb.minichefy.presentation.screens.recipe.recipe_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.repository.RecipeRepository
import hu.tb.minichefy.presentation.ui.theme.SEARCH_BAR_WAIT_AFTER_CHARACTER
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    data class UiState(
        val recipeList: List<Recipe> = emptyList(),
        val selectedRecipeId: Long? = null,
        val searchRecipeText: String = ""
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    sealed class OnEvent {
        data class OpenRecipeSettingsPanel(val recipeId: Long) : OnEvent()
        data class SearchTextChange(val text: String) : OnEvent()
        data object DeleteRecipe : OnEvent()
    }

    init {
        viewModelScope.launch {
            repository.getAllRecipe().collectLatest { data ->
                _uiState.update {
                    it.copy(recipeList = data)
                }
            }
        }
    }

    fun onEvent(event: OnEvent) {
        when (event) {
            OnEvent.DeleteRecipe -> viewModelScope.launch {
                repository.deleteRecipe(uiState.value.selectedRecipeId!!)
            }

            is OnEvent.OpenRecipeSettingsPanel -> {
                _uiState.update {
                    it.copy(
                        selectedRecipeId = event.recipeId
                    )
                }
            }

            is OnEvent.SearchTextChange -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(searchRecipeText = event.text) }

                    delay(SEARCH_BAR_WAIT_AFTER_CHARACTER)

                    repository.searchRecipeTitle(event.text).collect { recipeList ->
                        _uiState.update {
                            it.copy(
                                recipeList = recipeList
                            )
                        }
                    }
                }
            }
        }
    }
}