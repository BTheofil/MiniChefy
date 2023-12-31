package hu.tb.minichefy.presentation.screens.recipe_create

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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRecipeViewModel @Inject constructor(
    private val repository: RecipeDatabaseRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    data class UiState(
        val pages: List<Pages> = listOf(Pages.BasicInformationPage(), Pages.StepsPage()),
        val targetPageIndex: Int = 0
    )

    private val _basicPageState = MutableStateFlow(Pages.BasicInformationPage())
    val basicPageState = _basicPageState.asStateFlow()

    private val _stepsPageState = MutableStateFlow(Pages.StepsPage())
    val stepsPageState = _stepsPageState.asStateFlow()

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

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    sealed class UiEvent {
        data object OnNextPageClick : UiEvent()
        data object OnPreviousPage : UiEvent()
        data object OnRecipeCreateFinish : UiEvent()
    }

    sealed class OnEvent {
        //all pages event
        data object OnNextPageClick : OnEvent()
        data object OnPreviousPageBack : OnEvent()

        //basic page
        data class OnQuantityChange(val value: Int) : OnEvent()
        data class OnRecipeTitleChange(val text: String) : OnEvent()
        data class OnStepsFieldChange(val text: String) : OnEvent()

        //steps page
        data class OnAddRecipeStep(val stepDescription: String) : OnEvent()
        data class OnDeleteRecipeStep(val index: Int) : OnEvent()
        data object OnRecipeSave : OnEvent()
    }

    fun onEvent(event: OnEvent) {
        when (event) {
            //all pages event
            OnEvent.OnNextPageClick -> viewModelScope.launch {
                _uiState.update {
                    it.copy(targetPageIndex = it.targetPageIndex + 1)
                }
                _uiEvent.send(UiEvent.OnNextPageClick)
            }

            OnEvent.OnPreviousPageBack -> viewModelScope.launch {
                _uiState.value = uiState.value.copy(
                    targetPageIndex = uiState.value.targetPageIndex - 1
                )
                _uiEvent.send(UiEvent.OnPreviousPage)
            }

            //basic page
            is OnEvent.OnQuantityChange -> _basicPageState.update {
                it.copy(quantityCounter = it.quantityCounter + event.value)
            }

            is OnEvent.OnRecipeTitleChange -> _basicPageState.update {
                it.copy(recipeName = event.text)
            }

            is OnEvent.OnStepsFieldChange -> _stepsPageState.update {
                it.copy(typeField = event.text)
            }

            //steps page
            is OnEvent.OnAddRecipeStep -> {
                val updatedList = _stepsPageState.value.recipeSteps.toMutableList().apply {
                    add(RecipeStep(step = event.stepDescription))
                }
                _stepsPageState.update {
                    it.copy(
                        recipeSteps = updatedList,
                        typeField = ""
                    )
                }
            }

            is OnEvent.OnDeleteRecipeStep -> {
                val updatedList = _stepsPageState.value.recipeSteps.toMutableList().apply {
                    removeAt(event.index)
                }
                _stepsPageState.update {
                    it.copy(recipeSteps = updatedList)
                }
            }

            OnEvent.OnRecipeSave -> viewModelScope.launch {
                val createdRecipe = Recipe(
                    name = _basicPageState.value.recipeName,
                    quantity = _basicPageState.value.quantityCounter,
                    howToSteps = emptyList()
                )
                val resultId = repository.saveRecipe(createdRecipe)
                _stepsPageState.value.recipeSteps.forEach {
                    repository.saveStep(it, resultId)
                }
                _uiEvent.send(UiEvent.OnRecipeCreateFinish)
            }
        }
    }
}