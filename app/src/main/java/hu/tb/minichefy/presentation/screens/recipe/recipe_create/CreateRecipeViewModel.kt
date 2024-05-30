package hu.tb.minichefy.presentation.screens.recipe.recipe_create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.R
import hu.tb.minichefy.domain.model.recipe.RecipeIngredient
import hu.tb.minichefy.domain.model.recipe.RecipeStep
import hu.tb.minichefy.domain.model.recipe.TimeUnit
import hu.tb.minichefy.domain.model.storage.FoodSummary
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.domain.repository.RecipeRepository
import hu.tb.minichefy.domain.repository.StorageRepository
import hu.tb.minichefy.domain.use_case.ValidationResult
import hu.tb.minichefy.domain.use_case.Validators
import hu.tb.minichefy.presentation.util.icons.MealIcon
import hu.tb.minichefy.presentation.ui.theme.SEARCH_BAR_WAIT_AFTER_CHARACTER
import hu.tb.minichefy.domain.model.IconResource
import hu.tb.minichefy.presentation.screens.recipe.recipe_create.navigation.EDIT_RECIPE_ARGUMENT_KEY
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
    private val savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository,
    private val storageRepository: StorageRepository,
    private val validators: Validators,
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

    private var editRecipeId: Long? = null

    init {
        loadEditRecipe()
        viewModelScope.launch {
            _ingredientsPageState.update { ingredientsPage ->
                ingredientsPage.copy(
                    unSelectedIngredientList = storageRepository.getStorageIngredients()
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
            val selectedRecipeIcon: IconResource = MealIcon.entries[Random.nextInt(
                0,
                defaultIconCollection.size
            )],
            val timeField: String = "",
            val isTimeFieldHasError: Boolean = false,
            val timeUnit: TimeUnit = TimeUnit.MINUTES,
        ) : Pages()

        data class IngredientsPage(
            val selectedIngredientList: List<RecipeIngredient> = emptyList(),
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
        data class EmptyStepField(val messageResource: Int) : UiEvent()
        data class ErrorInRecipeFields(
            val isIngredientHasError: Boolean,
            val isStepsHasError: Boolean
        ) : UiEvent()
    }

    sealed class OnBasicInformationPageEvent {
        data class OnQuantityChange(val value: Int) : OnBasicInformationPageEvent()
        data class OnRecipeTitleChange(val text: String) : OnBasicInformationPageEvent()
        data class OnSelectedIconChange(val icon: IconResource) : OnBasicInformationPageEvent()
        data class OnTimeChange(val text: String) : OnBasicInformationPageEvent()
        data class OnTimeUnitChange(val unit: TimeUnit) : OnBasicInformationPageEvent()
    }

    sealed class OnIngredientEvent {
        data class OnSearchValueChange(val text: String) : OnIngredientEvent()
        data class OnIngredientTitleChange(val text: String) : OnIngredientEvent()
        data class OnIngredientQuantityChange(val quantityString: String) : OnIngredientEvent()
        data class OnIngredientUnitOfMeasurementChange(val unitOfMeasurement: UnitOfMeasurement) :
            OnIngredientEvent()

        data class OnPreMadeIngredientClick(
            val title: String,
            val unitOfMeasurement: UnitOfMeasurement
        ) : OnIngredientEvent()

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
                when (validators.validateQuantity((basicPageState.value.quantityCounter + event.value))) {
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
                it.copy(selectedRecipeIcon = event.icon)
            }

            is OnBasicInformationPageEvent.OnTimeChange -> {
                if (validators.validateNumberKeyboard(event.text) == ValidationResult.ERROR) return
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
                if (checkAddedIngredientValid()) {
                    return
                }

                val currentState = ingredientsPageState.value
                val updatedSelectedIngredients = currentState.selectedIngredientList.toMutableList()
                val temp = RecipeIngredient(
                    title = currentState.ingredientTitleDraft,
                    quantity = currentState.ingredientQuantityDraft.toFloat(),
                    unitOfMeasurement = currentState.ingredientUnitOfMeasurementDraft
                )

                if (temp !in updatedSelectedIngredients) {
                    updatedSelectedIngredients.add(0, temp)
                }

                _ingredientsPageState.value = currentState.copy(
                    selectedIngredientList = updatedSelectedIngredients,
                    isIngredientTitleHasError = false,
                    isIngredientQuantityHasError = false,
                    ingredientTitleDraft = "",
                    ingredientQuantityDraft = "",
                )
            }

            is OnIngredientEvent.OnPreMadeIngredientClick -> {
                _ingredientsPageState.update {
                    it.copy(
                        ingredientTitleDraft = event.title,
                        ingredientUnitOfMeasurementDraft = event.unitOfMeasurement
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
                            unSelectedIngredientList = storageRepository.searchIngredientsByLikelyTitle(
                                event.text
                            )
                        )
                    }
                }
            }

            is OnIngredientEvent.OnIngredientQuantityChange -> {
                if (validators.validateNumberKeyboard(event.quantityString) == ValidationResult.ERROR) return
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
                stepsPageState.value.recipeSteps.forEach {
                    if (validators.validateTextField(it.step) == ValidationResult.ERROR) {
                        viewModelScope.launch {
                            _uiEvent.send(
                                UiEvent.EmptyStepField(
                                    messageResource = R.string.one_or_more_step_fields_are_empty
                                )
                            )
                        }
                        return
                    }
                }

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
                if (checkRecipeValid()) {
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

    private fun loadEditRecipe() {
        try {
            val recipeId: String = checkNotNull(savedStateHandle[EDIT_RECIPE_ARGUMENT_KEY])
            editRecipeId = recipeId.toLong()
            viewModelScope.launch {
                val recipe = recipeRepository.getRecipeById(editRecipeId!!)

                _basicPageState.update {
                    it.copy(
                        recipeTitle = recipe.title,
                        quantityCounter = recipe.quantity,
                        selectedRecipeIcon = recipe.icon,
                        timeField = recipe.timeToCreate.toString(),
                        timeUnit = recipe.timeUnit
                    )
                }

                _ingredientsPageState.update {
                    it.copy(
                        selectedIngredientList = recipe.ingredientList,
                    )
                }

                _stepsPageState.update { it.copy(
                    recipeSteps = recipe.howToSteps
                ) }
            }
        } catch (_: IllegalStateException){
            //not came for edit recipe click
        }
    }

    private suspend fun saveRecipe(): Long =
        recipeRepository.saveRecipe(
            id = editRecipeId,
            icon = basicPageState.value.selectedRecipeIcon.resource.toString(),
            title = basicPageState.value.recipeTitle,
            quantity = basicPageState.value.quantityCounter,
            timeToCreate = basicPageState.value.timeField.toInt(),
            timeUnit = basicPageState.value.timeUnit,
        )

    private suspend fun saveIngredients(recipeId: Long) =
        ingredientsPageState.value.selectedIngredientList.forEach {
            recipeRepository.saveIngredient(it, recipeId)
        }

    private suspend fun saveSteps(recipeId: Long) =
        stepsPageState.value.recipeSteps.forEach { step ->
            recipeRepository.saveStep(step, recipeId)
        }

    private fun checkRecipeValid(): Boolean {
        val titleResult = validators.validateTextField(basicPageState.value.recipeTitle)
        val quantityCounterResult =
            validators.validateQuantity(basicPageState.value.quantityCounter)
        val timeResult = try {
            validators.validateQuantity(basicPageState.value.timeField.toInt())
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

    private fun checkAddedIngredientValid(): Boolean {
        val currentState = ingredientsPageState.value

        val titleResult =
            validators.validateTextField(currentState.ingredientTitleDraft)
        val quantityResult = try {
            validators.validateQuantity(currentState.ingredientQuantityDraft.toFloat())
        } catch (e: Exception) {
            ValidationResult.ERROR
        }

        val validationResults = listOf(
            titleResult, quantityResult
        ).any { it == ValidationResult.ERROR }

        if (validationResults) {
            _ingredientsPageState.value = currentState.copy(
                isIngredientTitleHasError = titleResult == ValidationResult.ERROR,
                isIngredientQuantityHasError = quantityResult == ValidationResult.ERROR,
            )
        }

        return validationResults
    }
}