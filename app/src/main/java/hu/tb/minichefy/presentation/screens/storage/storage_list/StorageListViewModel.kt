package hu.tb.minichefy.presentation.screens.storage.storage_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.domain.repository.StorageRepository
import hu.tb.minichefy.domain.use_case.ValidateNumberKeyboard
import hu.tb.minichefy.domain.use_case.ValidateQuantity
import hu.tb.minichefy.domain.use_case.ValidationResult
import hu.tb.minichefy.presentation.screens.manager.icons.IconResource
import hu.tb.minichefy.presentation.screens.manager.icons.FoodIcon
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
    private val validateQuantity: ValidateQuantity,
    private val validateNumberKeyboard: ValidateNumberKeyboard
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _editFoodState = MutableStateFlow(ModifyFoodQuantityDialogState())
    val editFoodState: StateFlow<ModifyFoodQuantityDialogState> = _editFoodState

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

    data class UiState(
        val searchText: String = "",
        val foodTagList: List<FoodTag> = emptyList(),
        val activeFilter: List<FoodTag> = emptyList(),
        val foodList: List<Food> = emptyList(),
        val allFoodIconList: List<FoodIcon> = FoodIcon.entries,
    )

    data class ModifyFoodQuantityDialogState(
        val modifyFoodListPositionIndex: Int = -1,
        val modifyFood: Food? = null,
        val quantityModifyValue: String = "",
        val unitOfMeasurementModifyValue: UnitOfMeasurement = UnitOfMeasurement.PIECE,
        val isQuantityModifyDialogHasError: Boolean = false,
    )

    sealed class UiEvent {
        data object CloseEditQuantityDialog : UiEvent()
        data object OpenEditQuantityDialog : UiEvent()
    }

    sealed class OnEvent {
        data object ClearSearchText : OnEvent()
        data object ClearSelectedFoodIndex : OnEvent()
        data object SaveEditFoodQuantities : OnEvent()
        data object SetupEditFoodQuantityDialog : OnEvent()
        data class DeleteFood(val foodId: Long) : OnEvent()
        data class SearchTextChange(val text: String) : OnEvent()
        data class FoodEditButtonClick(val positionIndex: Int) : OnEvent()
        data class FilterChipClicked(val tag: FoodTag) : OnEvent()
        data class ModifyFoodTags(val tag: FoodTag) : OnEvent()
        data class ModifyFoodIcon(val icon: IconResource) : OnEvent()
        data class EditFoodDialogQuantityChange(val value: String) : OnEvent()
        data class EditFoodDialogUnitChange(val value: UnitOfMeasurement) : OnEvent()
    }

    fun onEvent(event: OnEvent) {
        when (event) {
            //page events
            is OnEvent.SearchTextChange -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(searchText = event.text)
                    }

                    delay(SEARCH_BAR_WAIT_AFTER_CHARACTER)

                    val searchResult = storageRepository.searchKnownFoodByTitle(event.text)
                    _uiState.update {
                        it.copy(foodList = searchResult)
                    }
                }
            }

            OnEvent.ClearSearchText -> _uiState.update {
                it.copy(searchText = "")
            }

            is OnEvent.FilterChipClicked -> {
                val temp = uiState.value.activeFilter.toMutableList()
                if (uiState.value.activeFilter.contains(event.tag)) {
                    temp.remove(event.tag)
                } else {
                    temp.add(event.tag)
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
            is OnEvent.FoodEditButtonClick -> _editFoodState.update {
                it.copy(
                    modifyFoodListPositionIndex = event.positionIndex,
                    modifyFood = uiState.value.foodList[event.positionIndex]
                )
            }

            is OnEvent.EditFoodDialogQuantityChange -> {
                if (validateNumberKeyboard(event.value) == ValidationResult.ERROR) return

                _editFoodState.update {
                    it.copy(
                        quantityModifyValue = event.value
                    )
                }
            }

            is OnEvent.EditFoodDialogUnitChange -> _editFoodState.update {
                it.copy(
                    unitOfMeasurementModifyValue = event.value
                )
            }

            OnEvent.SetupEditFoodQuantityDialog -> {
                val editedFood = editFoodState.value.modifyFood!!

                _editFoodState.update {
                    it.copy(
                        quantityModifyValue = editedFood.quantity.toString(),
                        unitOfMeasurementModifyValue = editedFood.unitOfMeasurement
                    )
                }

                viewModelScope.launch {
                    _uiEvent.send(UiEvent.OpenEditQuantityDialog)
                }
            }

            OnEvent.SaveEditFoodQuantities -> {
                val quantityModifyValue = editFoodState.value.quantityModifyValue
                if (!quantityModifyValue.matches(Regex("^(?!.*\\..*\\.).*$")) || validateQuantity(
                        quantityModifyValue.toFloat()
                    ) == ValidationResult.ERROR
                ) {
                    _editFoodState.update {
                        it.copy(
                            isQuantityModifyDialogHasError = true
                        )
                    }
                    return
                }

                val updatedFood =
                    editFoodState.value.modifyFood!!.copy(
                        quantity = quantityModifyValue.toFloat(),
                        unitOfMeasurement = editFoodState.value.unitOfMeasurementModifyValue,
                    )
                _editFoodState.update {
                    it.copy(
                        isQuantityModifyDialogHasError = false
                    )
                }

                saveEditedFood(updatedFood)
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.CloseEditQuantityDialog)
                }
            }

            is OnEvent.ModifyFoodTags -> {
                viewModelScope.launch {
                    val modifyFood = editFoodState.value.modifyFood!!
                   /* if (modifyFood.foodTagList == null) {
                        storageRepository.saveFoodAndTag(modifyFood.id!!, event.tag.id!!)
                    } else {*/
                        if (modifyFood.foodTagList != null && modifyFood.foodTagList.contains(event.tag)) {
                            storageRepository.deleteFoodAndTag(modifyFood.id!!, event.tag.id!!)
                        } else {
                            storageRepository.saveFoodAndTag(modifyFood.id!!, event.tag.id!!)
                        }
                    //}
                }
            }

            OnEvent.ClearSelectedFoodIndex -> {
                _editFoodState.update {
                    it.copy(
                        modifyFoodListPositionIndex = -1,
                        modifyFood = null
                    )
                }
            }

            is OnEvent.ModifyFoodIcon -> saveEditedFood(
                editFoodState.value.modifyFood!!.copy(
                    icon = event.icon.resource
                )
            )

            is OnEvent.DeleteFood -> {
                viewModelScope.launch {
                    storageRepository.deleteFoodById(event.foodId)
                    storageRepository.deleteFoodAndTagsByFoodId(event.foodId)
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