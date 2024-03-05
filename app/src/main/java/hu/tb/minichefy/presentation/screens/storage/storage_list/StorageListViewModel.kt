package hu.tb.minichefy.presentation.screens.storage.storage_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.repository.StorageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageListViewModel @Inject constructor(
    private val storageRepository: StorageRepository
) : ViewModel() {

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
        val editedFood: Food? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    sealed class OnEvent {
        data object ClearSearch : OnEvent()
        data object SaveEditedFood : OnEvent()
        data class FoodUnitChanged(val food: Food, val change: Int) : OnEvent()
        data class SearchTextChange(val text: String) : OnEvent()
        data class EditFoodClicked(val food: Food) : OnEvent()
        data class FilterChipClicked(val tag: FoodTag) : OnEvent()
        data class ModifyEditedFoodTag(val tag: FoodTag) : OnEvent()
        data class ChangeQuantity(val value: Float) : OnEvent()
    }

    fun onEvent(event: OnEvent) {
        when (event) {
            is OnEvent.FoodUnitChanged -> viewModelScope.launch {
                val temp = event.food.copy(
                    quantity = event.food.quantity + event.change
                )

                storageRepository.saveOrModifyFood(food = temp)
            }

            is OnEvent.SearchTextChange -> _uiState.update {
                it.copy(searchText = event.text)
            }

            OnEvent.ClearSearch -> _uiState.update {
                it.copy(searchText = "")
            }

            is OnEvent.EditFoodClicked -> _uiState.update {
                it.copy(editedFood = event.food)
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

            is OnEvent.ChangeQuantity -> {

            }
            is OnEvent.ModifyEditedFoodTag -> {
                /*uiState.value.editedFood?.let { food ->
                    if(food.foodTagList != null){
                    } else if (temp)
                    try {
                        if (temp!!.contains(event.tag)){
                            temp.remove(event.tag)
                        } else {
                            temp.add(event.tag)
                        }
                    } catch (e: Exception){
                        temp.add(event.tag)
                    }
                    _uiState.update {
                        it.copy(
                            editedFood = food.copy(
                                foodTagList = temp
                            )
                        )
                    }
                }*/
            }

            OnEvent.SaveEditedFood -> {
                _uiState.update {
                    it.copy(editedFood = null)
                }
            }
        }
    }
}