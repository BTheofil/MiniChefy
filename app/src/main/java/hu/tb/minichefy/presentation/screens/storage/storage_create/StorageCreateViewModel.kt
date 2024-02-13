package hu.tb.minichefy.presentation.screens.storage.storage_create

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.domain.repository.StorageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class StorageCreateViewModel @Inject constructor(
    private val storageRepository: StorageRepository
) : ViewModel() {

    data class UiState(
        val foodTitleText: String = "",
        val foodType: FoodType? = null,
        val foodUnitOfMeasurement: List<UnitOfMeasurement> = UnitOfMeasurement.entries
    )

    enum class FoodType(val displayText: String){
        LIQUID("Liquid"),
        SOLID("Solid")
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    sealed class OnEvent {
        data class FoodTextChange(val text: String) : OnEvent()
        data class FoodTypeChange(val type: FoodType) : OnEvent()
        data class FoodUnitChange(val type: UnitOfMeasurement) : OnEvent()
    }

    fun onEvent(event: OnEvent) {
        when (event) {
            is OnEvent.FoodTextChange -> _uiState.update {
                it.copy(
                    foodTitleText = event.text
                )
            }

            is OnEvent.FoodTypeChange -> {
                when(event.type){
                    FoodType.LIQUID -> {
                        _uiState.update {
                            it.copy(foodUnitOfMeasurement = listOf(
                                UnitOfMeasurement.L,
                                UnitOfMeasurement.DL
                            ))
                        }
                    }
                    FoodType.SOLID -> _uiState.update {
                        it.copy(foodUnitOfMeasurement = listOf(
                            UnitOfMeasurement.KG,
                            UnitOfMeasurement.DKG,
                            UnitOfMeasurement.G,
                        ))
                    }
                }

                _uiState.update {
                    it.copy(foodType = event.type)
                }
            }
            is OnEvent.FoodUnitChange -> {

            }
        }
    }
}