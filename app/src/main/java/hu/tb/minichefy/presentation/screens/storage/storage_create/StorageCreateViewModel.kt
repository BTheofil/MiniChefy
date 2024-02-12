package hu.tb.minichefy.presentation.screens.storage.storage_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.domain.model.storage.entity.StorageFoodEntity
import hu.tb.minichefy.domain.repository.StorageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageCreateViewModel @Inject constructor(
    private val storageRepository: StorageRepository
) : ViewModel() {

    data class UiState(
        val id: Long? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    sealed class OnEvent {
        data object BtnClick : OnEvent()
        data object OtherBtnClick: OnEvent()
    }

    fun onEvent(event: OnEvent) {
        when (event) {
            OnEvent.BtnClick -> {
                viewModelScope.launch {
                    val id = storageRepository.saveOrModifyFoodEntity(
                        foodEntity = StorageFoodEntity(id = null, title = "apple", quantity = 1, unitOfMeasurement = UnitOfMeasurement.KG)
                    )

                    _uiState.update {
                        it.copy(id = id)
                    }
                }
            }

            OnEvent.OtherBtnClick -> {
                viewModelScope.launch {
                    storageRepository.saveOrModifyFoodEntity(
                        StorageFoodEntity(id = uiState.value.id, "apple", quantity = 5, unitOfMeasurement = UnitOfMeasurement.DKG)
                    )
                }
            }
        }
    }
}