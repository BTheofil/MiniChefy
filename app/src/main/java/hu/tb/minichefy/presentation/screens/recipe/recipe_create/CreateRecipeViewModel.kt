package hu.tb.minichefy.presentation.screens.recipe.recipe_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.recipe.RecipeStep
import hu.tb.minichefy.domain.model.recipe.TimeUnit
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodSummary
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.domain.model.storage.entity.UNKNOWN_TAG_ID
import hu.tb.minichefy.domain.repository.RecipeRepository
import hu.tb.minichefy.domain.repository.StorageRepository
import hu.tb.minichefy.domain.use_case.ValidateCountInteger
import hu.tb.minichefy.domain.use_case.ValidateNumberKeyboard
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
    private val validateCountInteger: ValidateCountInteger,
    private val validateTextField: ValidateTextField,
    private val validateNumberKeyboard: ValidateNumberKeyboard
) : ViewModel() {

    private val _basicPageState = MutableStateFlow(Pages.BasicInformationPage())
    val basicPageState = _basicPageState.asStateFlow()

    private val _ingredientsPageState = MutableStateFlow(Pages.IngredientsPage())
    val ingredientsPageState = _ingredientsPageState.asStateFlow()

    private val _stepsPageState = MutableStateFlow(Pages.StepsPage())
    val stepsPageState = _stepsPageState.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            _ingredientsPageState.update { ingredientsPage ->
                ingredientsPage.copy(
                    unSelectedIngredientList = storageRepository.getStorageFoodSummary()
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
    )

    sealed class Pages {
        data class BasicInformationPage(
            val recipeTitle: String = "",
            val isTitleHasError: Boolean = false,
            val quantityCounter: Int = 1,
            val isQuantityHasError: Boolean = false,
            val defaultIconCollection: List<MealIcon> = MealIcon.entries,
            val selectedMealIcon: MealIcon = MealIcon.entries[Random.nextInt(
                0,
                defaultIconCollection.size
            )],
            val timeField: String = "",
            val isTimeFieldHasError: Boolean = false,
            val timeUnit: TimeUnit = TimeUnit.MINUTES,
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

    sealed class UiEvent {
        data object RecipeSaved : UiEvent()
        data class ErrorInRecipeFields(
            val isIngredientHasError: Boolean,
            val isStepsHasError: Boolean
        ) : UiEvent()
    }

    sealed class OnBasicInformationPageEvent {
        data class OnQuantityChange(val value: Int) : OnBasicInformationPageEvent()
        data class OnRecipeTitleChange(val text: String) : OnBasicInformationPageEvent()
        data class OnSelectedIconChange(val icon: MealIcon) : OnBasicInformationPageEvent()
        data class OnTimeChange(val text: String) : OnBasicInformationPageEvent()
        data class OnTimeUnitChange(val unit: TimeUnit) : OnBasicInformationPageEvent()
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

    fun onBasicInformationPageEvent(event: OnBasicInformationPageEvent) {
        when (event) {
            is OnBasicInformationPageEvent.OnQuantityChange -> {
                when (validateCountInteger((basicPageState.value.quantityCounter + event.value))) {
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
                it.copy(recipeTitle = event.text)
            }

            is OnBasicInformationPageEvent.OnSelectedIconChange -> _basicPageState.update {
                it.copy(selectedMealIcon = event.icon)
            }

            is OnBasicInformationPageEvent.OnTimeChange -> {
                if (validateNumberKeyboard(event.text) == ValidationResult.ERROR) return
                _basicPageState.update {
                    it.copy(timeField = event.text)
                }
            }

            is OnBasicInformationPageEvent.OnTimeUnitChange -> _basicPageState.update {
                it.copy(timeUnit = event.unit)
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
                    currentState.ingredientQuantityDraft.isBlank() || validateCountInteger(
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

                viewModelScope.launch {
                    val foodTagList = mutableListOf<FoodTag>()
                    if (currentState.unSelectedIngredientList.find { it.title == currentState.ingredientTitleDraft } == null) {
                        val unknownTag = storageRepository.getTagById(UNKNOWN_TAG_ID.toLong())
                        foodTagList.add(unknownTag)
                    }

                    val temp = Food(
                        id = currentState.unSelectedIngredientList.find { it.title == currentState.ingredientTitleDraft }?.id,
                        title = currentState.ingredientTitleDraft,
                        quantity = currentState.ingredientQuantityDraft.toFloat(),
                        unitOfMeasurement = currentState.ingredientUnitOfMeasurementDraft,
                        icon = IconManager().getDefaultIcon.resource,
                        foodTagList = foodTagList
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
                            unSelectedIngredientList = storageRepository.searchFoodSummaryLikelyByTitle(
                                event.text
                            )
                        )
                    }
                }
            }

            is OnIngredientEvent.OnIngredientQuantityChange -> {
                if (validateNumberKeyboard(event.quantityString) == ValidationResult.ERROR) return
                _ingredientsPageState.update {
                    it.copy(
                        ingredientQuantityDraft = event.quantityString
                    )
                }
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

            OnStepsPageEvent.OnRecipeSave -> {
                if (checkRecipeDraftHasError()) {
                    return
                }

                viewModelScope.launch {
                    val recipeId = saveRecipe()
                    saveIngredients(recipeId)
                    saveSteps(recipeId)

                    _uiEvent.send(UiEvent.RecipeSaved)
                }
            }
        }
    }

    private suspend fun saveRecipe(): Long =
        recipeRepository.saveOrModifyRecipe(
            icon = basicPageState.value.selectedMealIcon.resource,
            title = basicPageState.value.recipeTitle,
            quantity = basicPageState.value.quantityCounter,
            timeToCreate = basicPageState.value.timeField.toInt(),
            timeUnit = basicPageState.value.timeUnit,
        )

    private suspend fun saveIngredients(recipeId: Long) {
        val (notCommonFoods, commonFoods) = ingredientsPageState.value.selectedIngredientList.partition { food ->
            ingredientsPageState.value.unSelectedIngredientList.none { it.title == food.title }
        }

        notCommonFoods.forEach { food ->
            val foodId = with(food) {
                storageRepository.saveOrModifyFood(
                    id = id,
                    title = title,
                    icon = icon,
                    quantity = quantity,
                    unitOfMeasurement = unitOfMeasurement,
                )
            }
            food.foodTagList?.forEach { tag -> storageRepository.saveFoodAndTag(foodId, tag.id!!) }
            recipeRepository.saveRecipeIngredientCrossRef(recipeId, foodId)
        }

        commonFoods.forEach { recipeRepository.saveRecipeIngredientCrossRef(recipeId, it.id!!) }
    }


    private suspend fun saveSteps(recipeId: Long){
        stepsPageState.value.recipeSteps.forEach { step ->
            recipeRepository.saveStep(step, recipeId)
            //Log.i("CreateRecipeVM", "StepId: $stepId")
        }
    }


    private fun checkRecipeDraftHasError(): Boolean {
        val titleResult = validateTextField(basicPageState.value.recipeTitle)
        val quantityCounterResult =
            validateCountInteger(basicPageState.value.quantityCounter)
        val timeResult = try {
            validateCountInteger(basicPageState.value.timeField.toInt())
        } catch (e: Exception) {
            ValidationResult.ERROR
        }

        val ingredientResult =
            if (ingredientsPageState.value.selectedIngredientList.isEmpty()) ValidationResult.ERROR else ValidationResult.SUCCESS
        var stepsResult = ValidationResult.SUCCESS
        for (step in stepsPageState.value.recipeSteps) {
            if (step.step.isEmpty()) {
                stepsResult = ValidationResult.ERROR
                break
            }
        }

        val validationResults = listOf(
            titleResult, timeResult, ingredientResult, stepsResult
        ).any { it == ValidationResult.ERROR }

        if (validationResults) {
            _basicPageState.update {
                it.copy(
                    isTitleHasError = titleResult == ValidationResult.ERROR,
                    isTimeFieldHasError = timeResult == ValidationResult.ERROR,
                    isQuantityHasError = quantityCounterResult == ValidationResult.ERROR
                )
            }
            viewModelScope.launch {
                _uiEvent.send(
                    UiEvent.ErrorInRecipeFields(
                        isIngredientHasError = ingredientResult == ValidationResult.ERROR,
                        isStepsHasError = stepsResult == ValidationResult.ERROR
                    )
                )
            }
        }

        return validationResults
    }
}