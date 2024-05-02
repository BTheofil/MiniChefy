package hu.tb.minichefy.presentation.screens.storage.storage_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.domain.repository.StorageRepository
import hu.tb.minichefy.domain.use_case.ValidationResult
import hu.tb.minichefy.domain.use_case.Validators
import hu.tb.minichefy.presentation.util.icons.FoodIcon
import hu.tb.minichefy.presentation.util.icons.IconManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageCreateViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    private val validators: Validators,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            storageRepository.getFoodTagFlow().collect { tagList ->
                _uiState.update {
                    it.copy(
                        labelFilterTagList = tagList
                    )
                }
            }
        }
    }

    data class UiState(
        val allFoodIcons: List<FoodIcon> = FoodIcon.entries,
        val selectedFoodIcon: FoodIcon = IconManager().getRandomFood(),
        val foodTitleText: String = "",
        val isFoodTitleHasError: Boolean = false,
        val foodUnitOfMeasurement: UnitOfMeasurement = UnitOfMeasurement.entries[1],
        val availableUnitOfMeasurementList: List<UnitOfMeasurement> = UnitOfMeasurement.entries,
        val quantity: String = "",
        val isQuantityHasError: Boolean = false,
        val selectedTagList: List<FoodTag> = emptyList(),
        val labelFilterTagList: List<FoodTag> = emptyList(),
        val tagDialogValue: String = ""
    )

    sealed class OnEvent {
        data object Save : OnEvent()
        data class FoodTextChange(val text: String) : OnEvent()
        data class OnSelectedIconDialogClick(val icon: FoodIcon) : OnEvent()
        data class FoodUnitChange(val type: UnitOfMeasurement) : OnEvent()
        data class FoodQuantityChange(val quantityString: String) : OnEvent()
        data class DialogChipClick(val editedTag: FoodTag) : OnEvent()
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
                    foodTitleText = event.text
                )
            }

            is OnEvent.FoodUnitChange -> {
                _uiState.update {
                    it.copy(
                        foodUnitOfMeasurement = event.type
                    )
                }
            }

            OnEvent.Save -> {
                if (checkFoodDraftHasError()) {
                    return
                }

                viewModelScope.launch {
                    uiState.value.also {
                        val foodId = storageRepository.saveOrModifyFood(
                            icon = it.selectedFoodIcon.resource,
                            title = it.foodTitleText,
                            quantity = it.quantity.toFloat(),
                            unitOfMeasurement = it.foodUnitOfMeasurement
                        )
                        it.selectedTagList.map { tag ->
                            storageRepository.saveFoodAndTag(foodId, tag.id!!)
                        }
                    }
                    _uiEvent.send(UiEvent.SaveSuccess)
                }
            }

            is OnEvent.DialogChipClick -> {
                val updatedTagList = uiState.value.selectedTagList.toMutableList().apply {
                    if (contains(event.editedTag)) remove(event.editedTag)
                    else add(event.editedTag)
                }

                _uiState.update { it.copy(selectedTagList = updatedTagList) }
            }

            is OnEvent.FoodQuantityChange -> {
                if (validators.validateNumberKeyboard(event.quantityString) == ValidationResult.ERROR) return

                _uiState.update {
                    it.copy(
                        quantity = event.quantityString
                    )
                }
            }

            is OnEvent.OnSelectedIconDialogClick -> _uiState.update {
                it.copy(selectedFoodIcon = event.icon)
            }
        }
    }

    private fun checkFoodDraftHasError(): Boolean {
        val titleResult = validators.validateTextField(uiState.value.foodTitleText)
        val quantityResult = try {
            validators.validateQuantity(uiState.value.quantity.toFloat())
        } catch (e: Exception) {
            ValidationResult.ERROR
        }

        val hasError =
            listOf(titleResult, quantityResult).any { it == ValidationResult.ERROR }
        if (hasError) {
            _uiState.update {
                it.copy(
                    isFoodTitleHasError = titleResult == ValidationResult.ERROR,
                    isQuantityHasError = quantityResult == ValidationResult.ERROR
                )
            }
        }
        return hasError
    }
}