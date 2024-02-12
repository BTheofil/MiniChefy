package hu.tb.minichefy.presentation.screens.storage.storage_list

import androidx.lifecycle.ViewModel
import hu.tb.minichefy.domain.model.storage.Food
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StorageListViewModel: ViewModel() {

    data class UiState(
        val searchText: String = "",
        val filterList: List<String> = emptyList(),
        val foodList: List<Food> = emptyList()
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    sealed class OnEvent{
        data class FoodUnitChanged(val foodId: Long, val change: Int): OnEvent()
    }

    fun onEvent(event: OnEvent){
        when(event){
            is OnEvent.FoodUnitChanged -> TODO()
        }
    }
}