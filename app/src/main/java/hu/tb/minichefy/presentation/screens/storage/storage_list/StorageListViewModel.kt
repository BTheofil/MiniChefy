package hu.tb.minichefy.presentation.screens.storage.storage_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.domain.repository.StorageRepository
import hu.tb.minichefy.presentation.screens.manager.icons.IconManager
import hu.tb.minichefy.presentation.screens.manager.icons.IconResource
import hu.tb.minichefy.presentation.screens.manager.icons.ProductIcon
import hu.tb.minichefy.presentation.ui.theme.SEARCH_BAR_WAIT_AFTER_CHARACTER
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageListViewModel @Inject constructor(
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _editFoodState = MutableStateFlow(ModifyFoodState())
    val editFoodState: StateFlow<ModifyFoodState> = _editFoodState

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
        val allProductIconList: List<ProductIcon> = IconManager().getAllProductIconList,
        val quantityModifyValue: String = "",
        val isQuantityModifyDialogHasError: Boolean = false
    )

    data class ModifyFoodState(
        val foodListPositionIndex: Int = -1,
        val quantityModifyValue: String = "",
        val unitOfMeasurementModifyValue: UnitOfMeasurement = UnitOfMeasurement.PIECE,
        val isQuantityModifyDialogHasError: Boolean = false,
    )

    sealed class OnEvent {
        data object ClearSearchText : OnEvent()
        data object ClearEditFoodDialog : OnEvent()
        data object ClearSelectedFoodIndex : OnEvent()
        data object SaveEditFoodQuantities : OnEvent()
        data object SetupEditFoodQuantityDialog : OnEvent()
        data class DeleteFood(val foodId: Long) : OnEvent()
        data class SearchTextChange(val text: String) : OnEvent()
        data class OnProductClick(val positionIndex: Int) : OnEvent()
        data class FilterChipClicked(val tag: FoodTag) : OnEvent()
        data class ModifyFoodTags(val tag: FoodTag) : OnEvent()
        data class ModifyFoodIcon(val icon: IconResource) : OnEvent()
        data class EditFoodQuantityDialog(val value: String) : OnEvent()
        data class EditFoodUnitOfMeasurementDialog(val value: UnitOfMeasurement) : OnEvent()
    }

    fun onEvent(event: OnEvent) {
        when (event) {
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

            is OnEvent.OnProductClick -> _editFoodState.update {
                it.copy(foodListPositionIndex = event.positionIndex)
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

            is OnEvent.EditFoodQuantityDialog -> _editFoodState.update {
                it.copy(
                    quantityModifyValue = event.value
                )
            }

            is OnEvent.EditFoodUnitOfMeasurementDialog -> _editFoodState.update {
                it.copy(
                    unitOfMeasurementModifyValue = event.value
                )
            }

            OnEvent.ClearEditFoodDialog -> _editFoodState.update {
                it.copy(
                    quantityModifyValue = "",
                )
            }

            OnEvent.SetupEditFoodQuantityDialog -> {
                val editedFood = uiState.value.foodList[editFoodState.value.foodListPositionIndex]

                _editFoodState.update {
                    it.copy(
                        quantityModifyValue = editedFood.quantity.toString(),
                        unitOfMeasurementModifyValue = editedFood.unitOfMeasurement
                    )
                }
            }

            OnEvent.SaveEditFoodQuantities -> {


                val updatedFood =
                    uiState.value.foodList[editFoodState.value.foodListPositionIndex].copy(
                        quantity = editFoodState.value.quantityModifyValue.toFloat(),
                        unitOfMeasurement = editFoodState.value.unitOfMeasurementModifyValue
                    )

                saveEditedFood(updatedFood)
            }

            is OnEvent.ModifyFoodTags -> {
                viewModelScope.launch {
                    val modifyFood =
                        uiState.value.foodList[editFoodState.value.foodListPositionIndex]
                    if (modifyFood.foodTagList == null) {
                        storageRepository.saveFoodAndTag(modifyFood.id!!, event.tag.id!!)
                    } else {
                        if (modifyFood.foodTagList.contains(event.tag)) {
                            storageRepository.deleteFoodAndTag(modifyFood.id!!, event.tag.id!!)
                        } else {
                            storageRepository.saveFoodAndTag(modifyFood.id!!, event.tag.id!!)
                        }
                    }
                }
            }

            OnEvent.ClearSelectedFoodIndex -> {
                _editFoodState.update { it.copy(foodListPositionIndex = -1) }
            }

            is OnEvent.ModifyFoodIcon -> saveEditedFood(
                uiState.value.foodList[editFoodState.value.foodListPositionIndex].copy(
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