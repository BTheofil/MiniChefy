package hu.tb.minichefy.presentation.screens.recipe.recipe_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.recipe.RecipeStep
import hu.tb.minichefy.domain.model.recipe.TimeUnit
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodSummary
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.domain.repository.RecipeRepository
import hu.tb.minichefy.domain.repository.StorageRepository
import hu.tb.minichefy.domain.use_case.ValidateQuantityNumber
import hu.tb.minichefy.domain.use_case.ValidateTextField
import hu.tb.minichefy.domain.use_case.ValidationResult
import hu.tb.minichefy.presentation.screens.manager.icons.IconManager
import hu.tb.minichefy.presentation.screens.manager.icons.MealIcon
import hu.tb.minichefy.presentation.ui.theme.SEARCH_BAR_WAIT_AFTER_CHARACTER
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CreateRecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val storageRepository: StorageRepository,
    private val validateQuantityNumber: ValidateQuantityNumber,
    private val validateTextField: ValidateTextField,
) : ViewModel() {

    private val _basicPageState = MutableStateFlow(Pages.BasicInformationPage())
    val basicPageState = _basicPageState.asStateFlow()

    private val _ingredientsPageState = MutableStateFlow(Pages.IngredientsPage())
    val ingredientsPageState = _ingredientsPageState.asStateFlow()

    private val _stepsPageState = MutableStateFlow(Pages.StepsPage())
    val stepsPageState = _stepsPageState.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _ingredientsPageState.update { ingredientsPage ->
                ingredientsPage.copy(
                    unSelectedIngredientList = storageRepository.getAllStorageFoodName()
                )
            }
        }
    }

    data class UiState(
        val pages: List<Pages> = listOf(
            Pages.BasicInformationPage(),
            Pages.IngredientsPage(),
            Pages.StepsPage()
        ),
        val targetPageIndex: Int = 0
    )

    sealed class Pages {
        data class BasicInformationPage(
            val recipeName: String = "",
            val quantityCounter: Int = 1,
            val isQuantityHasError: Boolean = false,
            val defaultIconCollection: List<MealIcon> = IconManager().getAllSystemMealIconLists,
            val selectedMealIcon: MealIcon = defaultIconCollection[Random.nextInt(
                0,
                defaultIconCollection.size
            )]
        ) : Pages()

        data class IngredientsPage(
            val selectedIngredientList: List<Food> = emptyList(),
            val unSelectedIngredientList: List<FoodSummary> = emptyList(),
            val searchText: String = "",
            val isIngredientTitleHasError: Boolean = false,
            val isIngredientQuantityHasError: Boolean = false,
            val ingredientTitleDraft: String = "",
            val ingredientQuantityDraft: String = "",
            val ingredientUnitOfMeasurementDraft: UnitOfMeasurement = UnitOfMeasurement.PIECE
        ) : Pages()

        data class StepsPage(
            val stepBoxTextField: String = "",
            val recipeSteps: List<RecipeStep> = listOf(RecipeStep(step = ""))
        ) : Pages()
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    sealed class UiEvent {
        data object PageChange : UiEvent()
    }

    sealed class OnEvent {
        data class PageChange(val pageIndex: Int) : OnEvent()
    }

    sealed class OnBasicInformationPageEvent {
        data class OnQuantityChange(val value: Int) : OnBasicInformationPageEvent()
        data class OnRecipeTitleChange(val text: String) : OnBasicInformationPageEvent()
        data class OnSelectedIconChange(val icon: MealIcon) : OnBasicInformationPageEvent()
    }

    sealed class OnIngredientEvent {
        data class OnSearchValueChange(val text: String) : OnIngredientEvent()
        data class OnIngredientTitleChange(val text: String) : OnIngredientEvent()
        data class OnIngredientQuantityChange(val quantityString: String) : OnIngredientEvent()
        data class OnIngredientUnitOfMeasurementChange(val unitOfMeasurement: UnitOfMeasurement) :
            OnIngredientEvent()

        data class IngredientRemove(val itemPos: Int) : OnIngredientEvent()
        data object IngredientAdd : OnIngredientEvent()
    }

    sealed class OnStepsPageEvent {
        data class OnStepsFieldChange(val text: String, val index: Int) : OnStepsPageEvent()
        data class OnAddRecipeStepToList(val stepDescription: String) : OnStepsPageEvent()
        data class OnDeleteRecipeStep(val index: Int) : OnStepsPageEvent()
        data class RecipeItemClick(val index: Int) : OnStepsPageEvent()
        data object OnRecipeSave : OnStepsPageEvent()
        data object ClearStepField : OnStepsPageEvent()
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
        }
    }

    fun onBasicInformationPageEvent(event: OnBasicInformationPageEvent) {
        when (event) {
            is OnBasicInformationPageEvent.OnQuantityChange -> {
                when (validateQuantityNumber((basicPageState.value.quantityCounter + event.value))) {
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

            is OnBasicInformationPageEvent.OnRecipeTitleChange -> _basicPageState.update {
                it.copy(recipeName = event.text)
            }

            is OnBasicInformationPageEvent.OnSelectedIconChange -> _basicPageState.update {
                it.copy(selectedMealIcon = event.icon)
            }
        }
    }

    fun onIngredientPageEvent(event: OnIngredientEvent) {
        when (event) {
            OnIngredientEvent.IngredientAdd -> {
                val currentState = ingredientsPageState.value

                val titleResult =
                    validateTextField(currentState.ingredientTitleDraft) == ValidationResult.ERROR
                val quantityResult =
                    currentState.ingredientQuantityDraft.isBlank() || validateQuantityNumber(
                        currentState.ingredientQuantityDraft.toInt()
                    ) == ValidationResult.ERROR

                val hasError = listOf(
                    titleResult, quantityResult
                ).any { it }

                if (hasError) {
                    _ingredientsPageState.value = currentState.copy(
                        isIngredientTitleHasError = titleResult,
                        isIngredientQuantityHasError = quantityResult
                    )
                    return
                }

                val selectedMutableList = currentState.selectedIngredientList.toMutableList()

                val temp = Food(
                    title = currentState.ingredientTitleDraft,
                    quantity = currentState.ingredientQuantityDraft.toFloat(),
                    unitOfMeasurement = currentState.ingredientUnitOfMeasurementDraft,
                    icon = IconManager().getDefaultIcon.resource,
                    foodTagList = emptyList()
                )

                if (temp !in selectedMutableList) {
                    selectedMutableList.add(0, temp)
                }

                _ingredientsPageState.value = currentState.copy(
                    selectedIngredientList = selectedMutableList,
                    isIngredientTitleHasError = false,
                    isIngredientQuantityHasError = false,
                    ingredientTitleDraft = "",
                    ingredientQuantityDraft = "",

                    )
            }

            is OnIngredientEvent.IngredientRemove -> {
                val updatedList = ingredientsPageState.value.selectedIngredientList.toMutableList()
                updatedList.removeAt(event.itemPos)

                _ingredientsPageState.update {
                    it.copy(
                        selectedIngredientList = updatedList
                    )
                }
            }

            is OnIngredientEvent.OnSearchValueChange -> {
                viewModelScope.launch {
                    _ingredientsPageState.update { it.copy(searchText = event.text) }

                    delay(SEARCH_BAR_WAIT_AFTER_CHARACTER)

                    _ingredientsPageState.update { ingredientsPage ->
                        ingredientsPage.copy(
                            unSelectedIngredientList = storageRepository.searchProductByTitle(
                                event.text
                            )
                        )
                    }
                }
            }

            is OnIngredientEvent.OnIngredientQuantityChange -> _ingredientsPageState.update {
                it.copy(
                    ingredientQuantityDraft = event.quantityString
                )
            }

            is OnIngredientEvent.OnIngredientTitleChange -> _ingredientsPageState.update {
                it.copy(
                    ingredientTitleDraft = event.text
                )
            }

            is OnIngredientEvent.OnIngredientUnitOfMeasurementChange -> _ingredientsPageState.update {
                it.copy(
                    ingredientUnitOfMeasurementDraft = event.unitOfMeasurement
                )
            }
        }
    }

    fun onStepsPageEvent(event: OnStepsPageEvent) {
        when (event) {
            is OnStepsPageEvent.OnStepsFieldChange -> {
                val updatedRecipeSteps =
                    stepsPageState.value.recipeSteps.toMutableList().apply {
                        this[event.index] = RecipeStep(step = event.text)
                    }
                _stepsPageState.update {
                    it.copy(recipeSteps = updatedRecipeSteps)
                }
            }

            is OnStepsPageEvent.RecipeItemClick -> {
                val selectedRecipeStep = stepsPageState.value.recipeSteps[event.index]
                _stepsPageState.update {
                    it.copy(
                        stepBoxTextField = selectedRecipeStep.step
                    )
                }
            }

            is OnStepsPageEvent.OnDeleteRecipeStep -> {
                val removedRecipeStepList =
                    stepsPageState.value.recipeSteps.toMutableList().apply {
                        removeAt(event.index)
                    }
                _stepsPageState.update {
                    it.copy(recipeSteps = removedRecipeStepList)
                }
            }

            is OnStepsPageEvent.OnAddRecipeStepToList -> {
                val recipeStepsPlusEmptyField =
                    stepsPageState.value.recipeSteps.toMutableList().apply {
                        add(RecipeStep(step = ""))
                    }
                _stepsPageState.update {
                    it.copy(recipeSteps = recipeStepsPlusEmptyField)
                }
            }

            OnStepsPageEvent.ClearStepField -> _stepsPageState.update { it.copy(stepBoxTextField = "") }

            OnStepsPageEvent.OnRecipeSave -> viewModelScope.launch {
                val createdRecipe = Recipe(
                    icon = basicPageState.value.selectedMealIcon.resource,
                    title = basicPageState.value.recipeName,
                    quantity = basicPageState.value.quantityCounter,
                    howToSteps = stepsPageState.value.recipeSteps,
                    timeToCreate = 0,
                    timeUnit = TimeUnit.MINUTES,
                    ingredientList = ingredientsPageState.value.selectedIngredientList.map {
                        Food(
                            id = it.id,
                            icon = IconManager().getDefaultIcon.resource,
                            title = it.title,
                            quantity = it.quantity,
                            unitOfMeasurement = it.unitOfMeasurement,
                            foodTagList = null
                        )
                    }
                )
                val resultId = recipeRepository.saveRecipe(createdRecipe)
                _stepsPageState.value.recipeSteps.forEach {
                    recipeRepository.saveStep(it, resultId)
                }
                if (_stepsPageState.value.stepBoxTextField.isNotBlank()) {
                    recipeRepository.saveStep(
                        RecipeStep(step = stepsPageState.value.stepBoxTextField),
                        resultId
                    )
                }
            }
        }
    }
}