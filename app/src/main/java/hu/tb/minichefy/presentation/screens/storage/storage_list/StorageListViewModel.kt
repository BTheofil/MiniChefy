package hu.tb.minichefy.presentation.screens.storage.storage_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.repository.StorageRepository
import hu.tb.minichefy.domain.use_case.CalculateMeasurements
import hu.tb.minichefy.domain.use_case.CalculationFood
import hu.tb.minichefy.presentation.screens.manager.icons.IconManager
import hu.tb.minichefy.presentation.screens.manager.icons.IconResource
import hu.tb.minichefy.presentation.screens.manager.icons.ProductIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageListViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    private val calculateMeasurements: CalculateMeasurements
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        viewModelScope.launch {
            storageRepository.getAllFood().collect { foodList ->
                _uiState.update {
                    it.copy(
                        foodList = foodList
                    )
                }
            }
        }
        viewModelScope.launch {
            storageRepository.getAllFoodTag().collect { tagList ->
                _uiState.update {
                    it.copy(foodTagList = tagList)
                }
            }
        }

    }

    data class UiState(
        val searchText: String = "",
        val foodTagList: List<FoodTag> = emptyList(),
        val activeFilter: List<FoodTag> = emptyList(),
        val foodList: List<Food> = emptyList(),
        val modifiedProductIndex: Int = -1,
        val allProductIconList: List<ProductIcon> = IconManager().getAllProductIconList
    )

    sealed class OnEvent {
        data object ClearSearchText : OnEvent()
        data object SaveEditedFood : OnEvent()
        data class SearchTextChange(val text: String) : OnEvent()
        data class OnProductClick(val index: Int) : OnEvent()
        data class FilterChipClicked(val tag: FoodTag) : OnEvent()
        data class ModifyProductTag(val tag: FoodTag) : OnEvent()
        data class ModifyProductQuantity(val value: Float) : OnEvent()
        data class ModifyProductIcon(val icon: IconResource) : OnEvent()
    }

    fun onEvent(event: OnEvent) {
        when (event) {
            is OnEvent.SearchTextChange -> _uiState.update {
                it.copy(searchText = event.text)
            }

            OnEvent.ClearSearchText -> _uiState.update {
                it.copy(searchText = "")
            }

            is OnEvent.OnProductClick -> _uiState.update {
                it.copy(modifiedProductIndex = event.index)
            }

            is OnEvent.FilterChipClicked -> {
                val temp = uiState.value.activeFilter.toMutableList()
                if (uiState.value.activeFilter.contains(event.tag)) {
                    temp.remove(event.tag)
                } else {
                    temp.add(event.tag)
                }
                _uiState.update {
                    it.copy(activeFilter = temp)
                }
            }

            is OnEvent.ModifyProductQuantity -> {
                val result = calculateMeasurements.simpleProductCalculations(
                    CalculationFood(
                        quantity = uiState.value.foodList[uiState.value.modifiedProductIndex].quantity,
                        unitOfMeasurement = uiState.value.foodList[uiState.value.modifiedProductIndex].unitOfMeasurement
                    ),
                    CalculationFood(
                        quantity = event.value,
                        unitOfMeasurement = uiState.value.foodList[uiState.value.modifiedProductIndex].unitOfMeasurement
                    )
                )
                val updatedFood =
                    uiState.value.foodList[uiState.value.modifiedProductIndex].copy(
                        quantity = result.quantity,
                        unitOfMeasurement = result.unitOfMeasurement
                    )
                saveEditedFood(updatedFood)
            }

            is OnEvent.ModifyProductTag -> {
                val foodTagList =
                    if (uiState.value.foodList[uiState.value.modifiedProductIndex].foodTagList.isNullOrEmpty()) {
                        listOf(event.tag)
                    } else {
                        val temp =
                            uiState.value.foodList[uiState.value.modifiedProductIndex].foodTagList!!.toMutableList()
                        if (temp.contains(event.tag)) temp.remove(event.tag) else temp.add(
                            event.tag
                        )
                        temp
                    }
                saveEditedFood(
                    uiState.value.foodList[uiState.value.modifiedProductIndex].copy(
                        foodTagList = foodTagList
                    )
                )
            }

            OnEvent.SaveEditedFood -> {
                _uiState.update { it.copy(modifiedProductIndex = -1) }
            }

            is OnEvent.ModifyProductIcon -> saveEditedFood(
                uiState.value.foodList[uiState.value.modifiedProductIndex].copy(
                    icon = event.icon.resource
                )
            )

        }
    }

    private fun saveEditedFood(updatedFood: Food) {
        viewModelScope.launch {
            storageRepository.saveOrModifyFood(
                id = updatedFood.id,
                title = updatedFood.title,
                icon = updatedFood.icon,
                quantity = updatedFood.quantity,
                unitOfMeasurement = updatedFood.unitOfMeasurement,
                tagListId = updatedFood.foodTagList?.map { it.id!! }
            )
        }
    }
}