package hu.tb.minichefy.presentation.screens.storage.storage_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.storage.Food
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
    }

    data class UiState(
        val searchText: String = "",
        val filterList: List<String> = emptyList(),
        val foodList: List<Food> = emptyList()
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    sealed class OnEvent {
        data object ClearSearch : OnEvent()
        data class FoodUnitChanged(val food: Food, val change: Int) : OnEvent()
        data class SearchTextChange(val text: String) : OnEvent()
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
        }
    }
}