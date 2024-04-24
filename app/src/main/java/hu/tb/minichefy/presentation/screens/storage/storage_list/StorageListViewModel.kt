package hu.tb.minichefy.presentation.screens.storage.storage_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.domain.repository.StorageRepository
import hu.tb.minichefy.domain.use_case.ValidationResult
import hu.tb.minichefy.domain.use_case.Validators
import hu.tb.minichefy.presentation.screens.manager.icons.FoodIcon
import hu.tb.minichefy.presentation.screens.manager.icons.IconResource
import hu.tb.minichefy.presentation.ui.theme.SEARCH_BAR_WAIT_AFTER_CHARACTER
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageListViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    private val validators: Validators,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            storageRepository.getKnownFoodsFlow().collect { foodList ->
                _uiState.update {
                    it.copy(
                        foodList = foodList,
                    )
                }
            }
        }
        viewModelScope.launch {
            val tags = storageRepository.getFilterableTagList()
            _uiState.update {
                it.copy(foodTagList = tags)
            }
        }
    }

    sealed class UiEvent {
        data object OpenModifyQuantityDialog : UiEvent()
        data object CloseModifyQuantityDialog : UiEvent()
    }

    data class UiState(
        val searchText: String = "",
        val foodTagList: List<FoodTag> = emptyList(),
        val activeFilter: List<FoodTag> = emptyList(),
        val foodList: List<Food> = emptyList(),
        val allFoodIconList: List<FoodIcon> = FoodIcon.entries,
        val modifyFoodListPositionIndex: Int = -1,
        val quantityModifyValue: String = "",
        val unitOfMeasurementModifyValue: UnitOfMeasurement = UnitOfMeasurement.PIECE,
        val isQuantityModifyDialogHasError: Boolean = false,
    )

    sealed class OnAction {
        data object ClearSelectedFoodIndex : OnAction()
        data object SaveEditFoodQuantities : OnAction()
        data object SetupEditFoodQuantityDialog : OnAction()
        data class DeleteFood(val foodId: Long) : OnAction()
        data class SearchTextChange(val text: String) : OnAction()
        data class FoodEditButtonClick(val positionIndex: Int) : OnAction()
        data class FilterChipClicked(val tag: FoodTag) : OnAction()
        data class ModifyFoodTags(val tag: FoodTag) : OnAction()
        data class ModifyFoodIcon(val icon: IconResource) : OnAction()
        data class ModifyFoodDialogQuantityChange(val value: String) : OnAction()
        data class EditFoodDialogUnitChange(val value: UnitOfMeasurement) : OnAction()
    }

    fun onAction(action: OnAction) {
        when (action) {
            //page events
            is OnAction.SearchTextChange -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(searchText = action.text)
                    }

                    delay(SEARCH_BAR_WAIT_AFTER_CHARACTER)

                    val searchResult = storageRepository.searchKnownFoodLikelyByTitle(action.text)
                    _uiState.update {
                        it.copy(foodList = searchResult)
                    }
                }
            }

            is OnAction.FilterChipClicked -> {
                val temp = uiState.value.activeFilter.toMutableList()
                if (uiState.value.activeFilter.contains(action.tag)) {
                    temp.remove(action.tag)
                } else {
                    temp.add(action.tag)
                }

                viewModelScope.launch {
                    val filteredFoodList = if (temp.isNotEmpty()) {
                        storageRepository.searchFoodsByTag(temp.map { it.id!! })
                    } else {
                        storageRepository.getKnownFoodList()
                    }

                    _uiState.update {
                        it.copy(
                            activeFilter = temp.toList(),
                            foodList = filteredFoodList
                        )
                    }
                }
            }

            //edit food events
            is OnAction.FoodEditButtonClick -> _uiState.update {
                it.copy(
                    modifyFoodListPositionIndex = action.positionIndex
                )
            }

            is OnAction.ModifyFoodDialogQuantityChange -> {
                if (validators.validateNumberKeyboard(action.value) == ValidationResult.ERROR) return

                _uiState.update {
                    it.copy(
                        quantityModifyValue = action.value
                    )
                }
            }

            is OnAction.EditFoodDialogUnitChange -> _uiState.update {
                it.copy(
                    unitOfMeasurementModifyValue = action.value
                )
            }

            OnAction.SetupEditFoodQuantityDialog -> {
                val editedFood = uiState.value.foodList[uiState.value.modifyFoodListPositionIndex]
                _uiState.update {
                    it.copy(
                        quantityModifyValue = editedFood.quantity.toString(),
                        unitOfMeasurementModifyValue = editedFood.unitOfMeasurement
                    )
                }
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.OpenModifyQuantityDialog)
                }
            }

            OnAction.SaveEditFoodQuantities -> {
                val quantityModifyValue = uiState.value.quantityModifyValue
                val quantityResult = try {
                    validators.validateQuantity(quantityModifyValue.toFloat())
                } catch (e: Exception) {
                    ValidationResult.ERROR
                }
                if (quantityResult == ValidationResult.ERROR) {
                    _uiState.update {
                        it.copy(
                            isQuantityModifyDialogHasError = true
                        )
                    }
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.OpenModifyQuantityDialog)
                    }
                    return
                }

                val updatedFood =
                    uiState.value.foodList[uiState.value.modifyFoodListPositionIndex].copy(
                        quantity = quantityModifyValue.toFloat(),
                        unitOfMeasurement = uiState.value.unitOfMeasurementModifyValue
                    )
                _uiState.update {
                    it.copy(
                        isQuantityModifyDialogHasError = false
                    )
                }

                saveEditedFood(updatedFood)
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.CloseModifyQuantityDialog)
                }
            }

            is OnAction.ModifyFoodTags -> {
                viewModelScope.launch {
                    val modifyFood =
                        uiState.value.foodList[uiState.value.modifyFoodListPositionIndex]
                    if (modifyFood.foodTagList != null && modifyFood.foodTagList.contains(action.tag)) {
                        storageRepository.deleteFoodAndTag(modifyFood.id!!, action.tag.id!!)
                    } else {
                        storageRepository.saveFoodAndTag(modifyFood.id!!, action.tag.id!!)
                    }
                }
            }

            is OnAction.ModifyFoodIcon -> saveEditedFood(
                uiState.value.foodList[uiState.value.modifyFoodListPositionIndex].copy(
                    icon = action.icon.resource
                )
            )

            OnAction.ClearSelectedFoodIndex -> {
                _uiState.update {
                    it.copy(
                        modifyFoodListPositionIndex = -1,
                        quantityModifyValue = "",
                    )
                }
            }

            is OnAction.DeleteFood -> {
                viewModelScope.launch {
                    storageRepository.deleteFoodById(action.foodId)
                    storageRepository.deleteFoodAndTagsByFoodId(action.foodId)
                }
            }
        }
    }

    private fun saveEditedFood(updatedFood: Food) {
        viewModelScope.launch {
            val foodId = storageRepository.saveOrModifyFood(
                id = updatedFood.id,
                title = updatedFood.title,
                icon = updatedFood.icon,
                quantity = updatedFood.quantity,
                unitOfMeasurement = updatedFood.unitOfMeasurement,
            )

            updatedFood.foodTagList?.map { tag ->
                storageRepository.saveFoodAndTag(foodId, tag.id!!)
            }
        }
    }
}