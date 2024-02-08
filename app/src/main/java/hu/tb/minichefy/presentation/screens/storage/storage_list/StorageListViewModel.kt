package hu.tb.minichefy.presentation.screens.storage.storage_list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StorageListViewModel: ViewModel() {

    data class UiState(
        val searchText: String = "",
        val filterList: List<String> = emptyList(),
        val foodList: List<String> = emptyList()
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState
}