package hu.tb.minichefy.presentation.screens.storage.storage_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
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
        val foodTitleText: String = "",
        val foodType: FoodType? = null,
        val foodUnitOfMeasurement: List<UnitOfMeasurement> = UnitOfMeasurement.entries,
        val selectedFoodUnitOfMeasurement: UnitOfMeasurement? = null,
        val tagList: List<String> = emptyList(),
        val tagDialogValue: String = ""
    )

    enum class FoodType(val displayText: String) {
        LIQUID("Liquid"),
        SOLID("Solid")
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    sealed class OnEvent {
        data object Save : OnEvent()
        data object AddTagToList: OnEvent()
        data class TagValueChange(val text: String) : OnEvent()
        data class FoodTextChange(val text: String) : OnEvent()
        data class FoodTypeChange(val type: FoodType) : OnEvent()
        data class FoodUnitChange(val type: UnitOfMeasurement) : OnEvent()
        data class RemoveTag(val index: Int) : OnEvent()
    }

    fun onEvent(event: OnEvent) {
        when (event) {
            is OnEvent.FoodTextChange -> _uiState.update {
                it.copy(
                    foodTitleText = event.text
                )
            }

            is OnEvent.FoodTypeChange -> {
                when (event.type) {
                    FoodType.LIQUID -> {
                        _uiState.update {
                            it.copy(
                                foodUnitOfMeasurement = listOf(
                                    UnitOfMeasurement.L,
                                    UnitOfMeasurement.DL
                                ),
                                selectedFoodUnitOfMeasurement = null
                            )
                        }
                    }

                    FoodType.SOLID -> _uiState.update {
                        it.copy(
                            foodUnitOfMeasurement = listOf(
                                UnitOfMeasurement.KG,
                                UnitOfMeasurement.DKG,
                                UnitOfMeasurement.G,
                            ),
                            selectedFoodUnitOfMeasurement = null
                        )
                    }
                }

                _uiState.update {
                    it.copy(foodType = event.type)
                }
            }

            is OnEvent.FoodUnitChange -> {
                _uiState.update {
                    it.copy(
                        selectedFoodUnitOfMeasurement = event.type
                    )
                }
            }

            OnEvent.Save -> {


                viewModelScope.launch {
                    uiState.value.also {
                        storageRepository.saveOrModifyFoodEntity(
                            Food(
                                title = it.foodTitleText,
                                quantity = 5,
                                unitOfMeasurement = it.selectedFoodUnitOfMeasurement!!,
                                type = ""
                            )
                        )
                    }
                }
            }

            is OnEvent.TagValueChange -> {
                _uiState.update {
                    it.copy(
                        tagDialogValue = event.text
                    )
                }
            }

            OnEvent.AddTagToList -> {
                val temp = uiState.value.tagList.toMutableList()
                temp.add(uiState.value.tagDialogValue)
                _uiState.update {
                    it.copy(
                        tagList = temp,
                        tagDialogValue = ""
                    )
                }

            }

            is OnEvent.RemoveTag -> {
                val temp = uiState.value.tagList.toMutableList()
                temp.removeAt(event.index)
                _uiState.update {
                    it.copy(
                        tagList = temp,
                        tagDialogValue = ""
                    )
                }
            }
        }
    }
}