package hu.tb.minichefy.presentation.screens.recipe.recipe_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.R
import hu.tb.minichefy.di.DISH_TAG_ID
import hu.tb.minichefy.domain.exceptions.NotCompatibleCalculation
import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.domain.repository.RecipeRepository
import hu.tb.minichefy.domain.repository.StorageRepository
import hu.tb.minichefy.domain.use_case.CalculateMeasurements
import hu.tb.minichefy.domain.use_case.CalculationFood
import hu.tb.minichefy.presentation.util.DataStoreManager
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.navigation.RECIPE_ID_ARGUMENT_KEY
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository,
    private val storageRepository: StorageRepository,
    private val dataStoreManager: DataStoreManager,
    private val calculateMeasurements: CalculateMeasurements,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadRecipe()
        loadInformationDialogShouldShow()
    }

    data class UiState(
        val recipe: Recipe? = null,
        val isInformDialogShouldShow: Boolean = true
    )

    sealed class UiEvent {
        data class ShowSnackBar(val messageResource: Int, val argument: String? = null) : UiEvent()
    }

    sealed class OnAction {
        data object MakeRecipe : OnAction()
        data class ShouldDialogAppear(val isDialogNeverShow: Boolean) : OnAction()
    }

    fun onAction(event: OnAction) {
        when (event) {
            is OnAction.MakeRecipe -> {
                viewModelScope.launch {
                    try {
                        modifyStorageFoodBasedOnIngredients()
                        addRecipeToStorage()
                        _uiEvent.send(UiEvent.ShowSnackBar(messageResource = R.string.dish_added_to_storage))
                    } catch (e: NotCompatibleCalculation) {
                        _uiEvent.send(
                            UiEvent.ShowSnackBar(
                                messageResource = R.string.can_not_make_the_meal_incompatible_ingredient_s,
                                argument = e.argument
                            )
                        )
                        return@launch
                    }
                }
            }

            is OnAction.ShouldDialogAppear -> if (event.isDialogNeverShow) {
                viewModelScope.launch {
                    dataStoreManager.setNeverShowDialogInDetailsScreen()
                }
            }
        }
    }

    private fun loadRecipe() {
        val recipeId: String = checkNotNull(savedStateHandle[RECIPE_ID_ARGUMENT_KEY])
        viewModelScope.launch {
            val recipe = recipeRepository.getRecipeById(recipeId.toLong())

            _uiState.update {
                it.copy(
                    recipe = recipe
                )
            }
        }
    }

    private fun loadInformationDialogShouldShow() {
        viewModelScope.launch {
            dataStoreManager.isDialogShouldShowInDetailsScreen()
                .collect { isDialogShouldShow ->
                    _uiState.update {
                        it.copy(
                            isInformDialogShouldShow = isDialogShouldShow
                        )
                    }
                }
        }
    }

    private suspend fun addRecipeToStorage() {
        val recipeInStorageMultipleNames =
            storageRepository.searchFoodByTitle(uiState.value.recipe!!.title)

        val result = if (recipeInStorageMultipleNames.isNotEmpty()) {
            recipeInStorageMultipleNames.first {
                it.foodTagList?.contains(storageRepository.getTagById(DISH_TAG_ID.toLong()))
                    ?: false
            }
        } else {
            null
        }

        val savedDishId = storageRepository.saveOrModifyFood(
            id = result?.id,
            icon = uiState.value.recipe!!.icon.resource.toString(),
            title = uiState.value.recipe!!.title,
            quantity = uiState.value.recipe!!.quantity + (result?.quantity ?: 0f),
            unitOfMeasurement = UnitOfMeasurement.PIECE,
        )

        val dishTag = storageRepository.getTagById(DISH_TAG_ID.toLong())
        storageRepository.saveFoodAndTag(savedDishId, dishTag.id!!)
    }

    private suspend fun modifyStorageFoodBasedOnIngredients() {
        val validCalculationFoodList = mutableListOf<Food>()

        uiState.value.recipe!!.ingredientList.forEach { food ->
            val storageFood = storageRepository.searchFoodByTitle(food.title).firstOrNull()
            if (storageFood != null) {
                val result = try {
                    calculateMeasurements.simpleProductCalculations(
                        productBase = CalculationFood(
                            storageFood.quantity,
                            storageFood.unitOfMeasurement
                        ),
                        productChanger = CalculationFood(
                            food.quantity * (-1),
                            food.unitOfMeasurement
                        )
                    )
                } catch (e: NotCompatibleCalculation) {
                    throw NotCompatibleCalculation(message = e.message, argument = food.title)
                }

                validCalculationFoodList.add(storageFood.copy(quantity = result.quantity,
                    unitOfMeasurement = result.unitOfMeasurement))
            }
        }

        validCalculationFoodList.forEach { food: Food ->
            storageRepository.saveOrModifyFood(
                id = food.id,
                title = food.title,
                icon = food.icon.resource.toString(),
                quantity = food.quantity,
                unitOfMeasurement = food.unitOfMeasurement
            )
        }
    }
}