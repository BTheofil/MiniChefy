package hu.tb.minichefy.presentation.screens.storage.storage_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.domain.repository.StorageRepository
import hu.tb.minichefy.presentation.screens.components.icons.IconManager
import hu.tb.minichefy.presentation.screens.components.icons.ProductIcon
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageCreateViewModel @Inject constructor(
    private val storageRepository: StorageRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            storageRepository.getAllFoodTag().collect { tags ->
                _uiState.update {
                    it.copy(
                        allProductTagList = tags
                    )
                }
            }
        }
    }

    data class UiState(
        val productIcon: ProductIcon = IconManager().getRandomProduct(),
        val productTitleText: String = "",
        val productType: FoodType? = FoodType.LIQUID,
        val productUnitOfMeasurement: UnitOfMeasurement = UnitOfMeasurement.entries[1],
        val availableUnitOfMeasurementList: List<UnitOfMeasurement> = UnitOfMeasurement.entries,
        val quantity: String = "",
        val selectedTagList: List<FoodTag> = emptyList(),
        val allProductTagList: List<FoodTag> = emptyList(),
        val tagDialogValue: String = ""
    )

    enum class FoodType(val displayText: String) {
        LIQUID("Liquid"),
        SOLID("Solid"),
        PIECE("Piece")
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    sealed class OnEvent {
        data object Save : OnEvent()
        data class FoodTextChange(val text: String) : OnEvent()
        data class FoodTypeChange(val type: FoodType) : OnEvent()
        data class FoodUnitChange(val type: UnitOfMeasurement) : OnEvent()
        data class FoodQuantityChange(val quantity: String) : OnEvent()
        data class DialogChipTouched(val editedTag: FoodTag) : OnEvent()
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    sealed class UiEvent {
        data object SaveSuccess : UiEvent()
    }

    fun onEvent(event: OnEvent) {
        when (event) {
            is OnEvent.FoodTextChange -> _uiState.update {
                it.copy(
                    productTitleText = event.text
                )
            }

            is OnEvent.FoodTypeChange -> {
                when (event.type) {
                    FoodType.LIQUID -> {
                        _uiState.update {
                            it.copy(
                                availableUnitOfMeasurementList = listOf(
                                    UnitOfMeasurement.DL,
                                    UnitOfMeasurement.L
                                ),
                                productUnitOfMeasurement = UnitOfMeasurement.DL
                            )
                        }
                    }

                    FoodType.SOLID -> _uiState.update {
                        it.copy(
                            availableUnitOfMeasurementList = listOf(
                                UnitOfMeasurement.G,
                                UnitOfMeasurement.DKG,
                                UnitOfMeasurement.KG,
                            ),
                            productUnitOfMeasurement = UnitOfMeasurement.G,
                        )
                    }

                    FoodType.PIECE -> {}
                }

                _uiState.update {
                    it.copy(productType = event.type)
                }
            }

            is OnEvent.FoodUnitChange -> {
                _uiState.update {
                    it.copy(
                        productUnitOfMeasurement = event.type
                    )
                }
            }

            OnEvent.Save -> {
                viewModelScope.launch {
                    uiState.value.also {
                        storageRepository.saveOrModifyFood(
                            Food(
                                icon = it.productIcon.resource,
                                title = it.productTitleText,
                                quantity = it.quantity.toFloat(),
                                unitOfMeasurement = it.productUnitOfMeasurement,
                                foodTagList = it.selectedTagList
                            )
                        )
                    }
                    _uiEvent.send(UiEvent.SaveSuccess)
                }
            }

            is OnEvent.DialogChipTouched -> {
                val updatedTagList = uiState.value.selectedTagList.toMutableList()
                if (uiState.value.selectedTagList.contains(event.editedTag)) {
                    updatedTagList.remove(event.editedTag)
                } else {
                    updatedTagList.add(event.editedTag)
                }
                _uiState.update {
                    it.copy(
                        selectedTagList = updatedTagList
                    )
                }
            }

            is OnEvent.FoodQuantityChange -> {
                val value = if(event.quantity.isEmpty() || event.quantity.isBlank() || event.quantity == "-"){
                    ""
                } else {
                    event.quantity
                }

                _uiState.update {
                    it.copy(
                        quantity = value
                    )
                }
            }
        }
    }
}