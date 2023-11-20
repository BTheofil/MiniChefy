package hu.tb.minichefy.presentation.screens.recipe_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.tb.minichefy.domain.model.RecipeStep
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CreateRecipeViewModel : ViewModel() {

    data class UiState(
        val pages: List<Pages> = listOf(Pages.BasicInformationPage(), Pages.StepsPage()),
        val targetPageIndex: Int = 0
    )
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    sealed class UiEvent {
        data object OnNextPageClick : UiEvent()
        data object OnPreviousPage : UiEvent()
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onNextPageClick() {
        _uiState.value = uiState.value.copy(
            targetPageIndex = uiState.value.targetPageIndex + 1
        )
        viewModelScope.launch {
            _uiEvent.send(UiEvent.OnNextPageClick)
        }
    }

    fun onPreviousPageBack(){
        _uiState.value = uiState.value.copy(
            targetPageIndex = uiState.value.targetPageIndex - 1
        )
        viewModelScope.launch {
            _uiEvent.send(UiEvent.OnPreviousPage)
        }
    }

    sealed class Pages {
        data class BasicInformationPage(
            val recipeName: String = "",
            val quantityCounter: Int = 0
        ) : Pages()

        data class StepsPage(
            val typeField: String = "",
            val recipeSteps: List<RecipeStep> = emptyList(),
            val recipeStepsVisible: List<Boolean> = emptyList()
        ) : Pages()
    }

    private val _basicPageState = MutableStateFlow(Pages.BasicInformationPage())
    val basicPageState = _basicPageState.asStateFlow()

    fun onQuantityChange(value: Int) {
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

        val updatedList2 = _stepsPageState.value.recipeStepsVisible.toMutableList().apply {
            add(true)
        }
        _stepsPageState.value = stepsPageState.value.copy(
            recipeSteps = updatedList,
            typeField = "",
            recipeStepsVisible = updatedList2
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