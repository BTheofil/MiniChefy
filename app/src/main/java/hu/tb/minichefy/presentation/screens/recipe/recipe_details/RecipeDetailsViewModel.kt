package hu.tb.minichefy.presentation.screens.recipe.recipe_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.domain.model.storage.entity.DISH_TAG_ID
import hu.tb.minichefy.domain.repository.RecipeRepository
import hu.tb.minichefy.domain.repository.StorageRepository
import hu.tb.minichefy.domain.use_case.CalculateMeasurements
import hu.tb.minichefy.domain.use_case.CalculationFood
import hu.tb.minichefy.domain.use_case.DataStoreManager
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.navigation.RECIPE_ID_ARGUMENT_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    init {
        loadRecipe()
        loadInformationDialogShouldShow()
    }

    data class UiState(
        val recipe: Recipe? = null,
        val isInformDialogShouldShow: Boolean = true
    )

    sealed class OnEvent {
        data object MakeRecipe : OnEvent()
        data class ShouldDialogAppear(val isDialogNeverShow: Boolean) : OnEvent()
    }

    fun onEvent(event: OnEvent) {
        when (event) {
            is OnEvent.MakeRecipe -> {
                viewModelScope.launch {
                    addRecipeToStorage()
                    modifyStorageFoodBasedOnIngredients()
                }
            }

            is OnEvent.ShouldDialogAppear -> if (event.isDialogNeverShow) {
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
            icon = uiState.value.recipe!!.icon,
            title = uiState.value.recipe!!.title,
            quantity = uiState.value.recipe!!.quantity + (result?.quantity
                ?: 0f),
            unitOfMeasurement = UnitOfMeasurement.PIECE,
        )

        val dishTag = storageRepository.getTagById(DISH_TAG_ID.toLong())
        storageRepository.saveFoodAndTag(savedDishId, dishTag.id!!)
    }

    private suspend fun modifyStorageFoodBasedOnIngredients() {
        uiState.value.recipe!!.ingredientList.forEach { food ->
            val storageFood = storageRepository.searchKnownFoodByTitle(food.title)
            if (storageFood.isNotEmpty()) {
                val result = calculateMeasurements.simpleProductCalculations(
                    productBase = CalculationFood(
                        storageFood.first().quantity,
                        storageFood.first().unitOfMeasurement
                    ),
                    productChanger = CalculationFood(food.quantity * (-1), food.unitOfMeasurement)
                )

                storageRepository.saveOrModifyFood(
                    id = storageFood.first().id,
                    title = storageFood.first().title,
                    icon = storageFood.first().icon,
                    quantity = result.quantity,
                    unitOfMeasurement = result.unitOfMeasurement
                )
            }

        }
    }
}