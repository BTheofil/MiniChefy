package hu.tb.minichefy.new_recipe

import androidx.lifecycle.ViewModel
import hu.tb.minichefy.domain.model.RecipeStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateRecipeViewModel : ViewModel() {

    data class UiState(
        val activeField: String = "",
        val recipeSteps: List<RecipeStep> = emptyList(),
    )

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    fun onFieldChange(text: String) {
        _state.value = _state.value.copy(
            activeField = text
        )
    }

    fun addRecipeStep(stepDescription: String) {
        val updatedList = _state.value.recipeSteps.toMutableList().apply {
            add(RecipeStep(state.value.recipeSteps.size, stepDescription))
        }
        _state.value = state.value.copy(
            recipeSteps = updatedList,
            activeField = "",
        )
    }

    fun removeRecipeStep(index: Int) {
        val updatedList = _state.value.recipeSteps.toMutableList().apply {
            removeAt(index)
        }

        _state.value = state.value.copy(
            recipeSteps = updatedList
        )
    }
}