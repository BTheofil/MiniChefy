package hu.tb.minichefy.presentation.screens.recipe.recipe_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.Recipe
import hu.tb.minichefy.domain.repository.RecipeRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

private const val RECIPE_SEARCH_DELAY = 500L

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    data class UiState(
        val recipeList: List<Recipe> = emptyList(),
        val selectedRecipeId: Long? = null,
        val showSettingsPanel: Boolean = false,
        val searchRecipeText: String = ""
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    sealed class UiEvent {
        data class OnRecipeClick(val recipeId: Long) : UiEvent()
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    sealed class OnEvent {
        data class OnRecipeClick(val recipeId: Long) : OnEvent()
        data class OpenRecipeSettingsPanel(val recipeId: Long) : OnEvent()
        data object DeleteRecipe : OnEvent()
        data class SearchTextChange(val text: String) : OnEvent()
    }

    private val allRecipes = repository.getAllRecipe()

    init {
        viewModelScope.launch {
            allRecipes.collectLatest { data ->
                _uiState.update {
                    it.copy(recipeList = data)
                }
            }
        }
    }

    fun onEvent(event: OnEvent) {
        when (event) {
            is OnEvent.OnRecipeClick -> viewModelScope.launch {
                _uiEvent.send(UiEvent.OnRecipeClick(event.recipeId))
            }

            OnEvent.DeleteRecipe -> viewModelScope.launch {
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

            is OnEvent.SearchTextChange -> {
                viewModelScope.launch {
                    _uiState.emit(_uiState.value.copy(searchRecipeText = event.text))

                    delay(RECIPE_SEARCH_DELAY.milliseconds)

                    _uiState.emit(
                        _uiState.value.copy(
                            recipeList = if (uiState.value.searchRecipeText.isNotBlank()) {
                                repository.searchRecipeByTitle(event.text)
                            } else {
                                repository.searchRecipeByTitle("")
                            }
                        )
                    )
                }
            }
        }
    }
}