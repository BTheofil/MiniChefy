package hu.tb.minichefy.presentation.screens.recipe_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.data.repository.RecipeMemoryRepositoryImpl
import hu.tb.minichefy.domain.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    recipeRepository: RecipeMemoryRepositoryImpl
) : ViewModel() {

    data class UiState(
        val recipe: Recipe? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        val recipeId: String = checkNotNull(savedStateHandle["recipeId"])
        val recipe = recipeRepository.getRecipeById(recipeId.toLong())
        _uiState.value = uiState.value.copy(
            recipe = recipe
        )
    }
}