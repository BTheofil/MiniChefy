package hu.tb.minichefy.presentation.screens.recipe.recipe_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.recipe.RecipeStep
import hu.tb.minichefy.domain.repository.RecipeRepository
import hu.tb.minichefy.domain.use_case.ValidateQuantityNumber
import hu.tb.minichefy.domain.use_case.ValidationResult
import hu.tb.minichefy.presentation.screens.recipe.components.IconManager
import hu.tb.minichefy.presentation.screens.recipe.components.FoodIcon
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CreateRecipeViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val validateQuantityNumber: ValidateQuantityNumber
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
            val quantityCounter: Int = 1,
            val isQuantityHasError: Boolean = false,
            val defaultIconCollection: List<FoodIcon> = IconManager().foodIcons,
            val selectedFoodIcon: FoodIcon = defaultIconCollection[Random.nextInt(
                0,
                defaultIconCollection.size
            )]
        ) : Pages()

        data class StepsPage(
            val stepTypeField: String = "",
            val recipeSteps: List<RecipeStep> = listOf(RecipeStep(step = ""))
        ) : Pages()
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    sealed class UiEvent {
        data object PageChange : UiEvent()
    }

    sealed class OnEvent {
        //all pages event
        data class PageChange(val pageIndex: Int) : OnEvent()

        //basic page
        data class OnQuantityChange(val value: Int) : OnEvent()
        data class OnRecipeTitleChange(val text: String) : OnEvent()
        data class OnSelectedIconChange(val icon: FoodIcon) : OnEvent()

        //steps page
        data class OnStepsFieldChange(val text: String, val index: Int) : OnEvent()
        data class OnAddRecipeStepToList(val stepDescription: String) : OnEvent()
        data class OnDeleteRecipeStep(val index: Int) : OnEvent()
        data class RecipeItemClick(val index: Int) : OnEvent()
        data object OnRecipeSave : OnEvent()
        data object ClearStepField : OnEvent()
    }

    fun onEvent(event: OnEvent) {
        when (event) {
            //all pages event
            is OnEvent.PageChange -> viewModelScope.launch {
                _uiState.update {
                    it.copy(targetPageIndex = event.pageIndex)
                }
                _uiEvent.send(UiEvent.PageChange)
            }

            //basic page
            is OnEvent.OnQuantityChange -> {
                when (validateQuantityNumber(basicPageState.value.quantityCounter + event.value)) {
                    ValidationResult.SUCCESS -> _basicPageState.update {
                        it.copy(
                            quantityCounter = it.quantityCounter + event.value,
                            isQuantityHasError = false
                        )
                    }

                    ValidationResult.ERROR -> _basicPageState.update {
                        it.copy(isQuantityHasError = true)
                    }
                }
            }

            is OnEvent.OnRecipeTitleChange -> _basicPageState.update {
                it.copy(recipeName = event.text)
            }


            is OnEvent.OnSelectedIconChange -> _basicPageState.update {
                it.copy(selectedFoodIcon = event.icon)
            }

            //steps page
            is OnEvent.OnStepsFieldChange -> {
                val updatedRecipeSteps = stepsPageState.value.recipeSteps.toMutableList().apply {
                    this[event.index] = RecipeStep(step = event.text)
                }
                _stepsPageState.update {
                    it.copy(recipeSteps = updatedRecipeSteps)
                }
            }

            is OnEvent.RecipeItemClick -> {
                val selectedRecipeStep = stepsPageState.value.recipeSteps[event.index]
                _stepsPageState.update {
                    it.copy(
                        stepTypeField = selectedRecipeStep.step
                    )
                }
            }

            is OnEvent.OnDeleteRecipeStep -> {
                val removedRecipeStepList = stepsPageState.value.recipeSteps.toMutableList().apply {
                    removeAt(event.index)
                }
                _stepsPageState.update {
                    it.copy(recipeSteps = removedRecipeStepList)
                }
            }

            is OnEvent.OnAddRecipeStepToList -> {
                val recipeStepsPlusEmptyField = stepsPageState.value.recipeSteps.toMutableList().apply {
                    add(RecipeStep(step = ""))
                }
                _stepsPageState.update {
                    it.copy(recipeSteps = recipeStepsPlusEmptyField)
                }
            }

            OnEvent.ClearStepField -> _stepsPageState.update { it.copy(stepTypeField = "") }

            OnEvent.OnRecipeSave -> viewModelScope.launch {
                val createdRecipe = Recipe(
                    icon = basicPageState.value.selectedFoodIcon.resource,
                    title = basicPageState.value.recipeName,
                    quantity = basicPageState.value.quantityCounter,
                    howToSteps = emptyList()
                )
                val resultId = repository.saveRecipe(createdRecipe)
                _stepsPageState.value.recipeSteps.forEach {
                    repository.saveStep(it, resultId)
                }
                if (_stepsPageState.value.stepTypeField.isNotBlank()) {
                    repository.saveStep(
                        RecipeStep(step = stepsPageState.value.stepTypeField),
                        resultId
                    )
                }
            }
        }
    }
}