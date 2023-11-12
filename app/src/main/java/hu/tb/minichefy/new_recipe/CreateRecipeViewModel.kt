package hu.tb.minichefy.new_recipe

import androidx.lifecycle.ViewModel
import hu.tb.minichefy.domain.model.RecipeStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateRecipeViewModel : ViewModel() {

    data class UiState(
        val pages: List<Pages> = listOf(Pages.BasicInformationPage(), Pages.StepsPage()),
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    sealed class Pages {
        data class BasicInformationPage(
            val recipeName: String = "",
            val quantityCounter: Int = 0
        ) : Pages()

        data class StepsPage(
            val typeField: String = "",
            val recipeSteps: List<RecipeStep> = emptyList()
        ) : Pages()
    }

    private val _basicPageState = MutableStateFlow(Pages.BasicInformationPage())
    val basicPageState = _basicPageState.asStateFlow()

    fun onQuantityChange(value: Int){
        _basicPageState.value = basicPageState.value.copy(
            quantityCounter = basicPageState.value.quantityCounter + value
        )
    }

    private val _stepsPageState = MutableStateFlow(Pages.StepsPage())
    val stepsPageState = _stepsPageState.asStateFlow()

    fun onFieldChange(text: String) {
        _stepsPageState.value = _stepsPageState.value.copy(
            typeField = text
        )
    }

    fun addRecipeStep(stepDescription: String) {
        val updatedList = _stepsPageState.value.recipeSteps.toMutableList().apply {
            add(RecipeStep(stepsPageState.value.recipeSteps.size, stepDescription))
        }
        _stepsPageState.value = stepsPageState.value.copy(
            recipeSteps = updatedList,
            typeField = "",
        )
    }

    fun removeRecipeStep(index: Int) {
        val updatedList = _stepsPageState.value.recipeSteps.toMutableList().apply {
            removeAt(index)
        }

        _stepsPageState.value = stepsPageState.value.copy(
            recipeSteps = updatedList
        )
    }
}