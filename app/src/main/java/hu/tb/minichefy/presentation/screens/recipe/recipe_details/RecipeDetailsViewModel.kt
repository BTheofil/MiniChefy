package hu.tb.minichefy.presentation.screens.recipe.recipe_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.data.repository.RecipeDatabaseRepositoryImpl
import hu.tb.minichefy.domain.model.Recipe
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.navigation.RECIPE_ID_ARGUMENT_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    recipeRepository: RecipeDatabaseRepositoryImpl
) : ViewModel() {

    data class UiState(
        val recipe: Recipe? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        val recipeId: String = checkNotNull(savedStateHandle[RECIPE_ID_ARGUMENT_KEY])
        viewModelScope.launch {
            recipeRepository.getRecipeById(recipeId.toLong()).collect { recipe ->
                _uiState.update {
                    it.copy(
                        recipe = recipe
                    )
                }
            }
        }
    }
}