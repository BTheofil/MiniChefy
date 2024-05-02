package hu.tb.minichefy.presentation.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.repository.StorageRepository
import hu.tb.minichefy.domain.use_case.ValidationResult
import hu.tb.minichefy.domain.use_case.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    private val validators: Validators,
) : ViewModel() {

    var foodTagList by mutableStateOf<List<FoodTag>>(emptyList())
        private set

    private var _tagState = MutableStateFlow(TagState())
    val tagState: StateFlow<TagState> = _tagState

    init {
        getTagList()
    }

    data class TagState(
        val tagText: String = "",
        val isValid: Boolean = true,
    )

    fun updateTagTextFieldValue(text: String) {
        _tagState.update {
            it.copy(tagText = text)
        }
    }

    fun saveNewTag() {
        if (validators.validateTextField(tagState.value.tagText) == ValidationResult.ERROR) {
            _tagState.update {
                it.copy(isValid = false)
            }
            return
        }

        viewModelScope.launch {
            storageRepository.saveOrModifyFoodTag(tag = FoodTag(tag = tagState.value.tagText))
            _tagState.update {
                it.copy(
                    tagText = "",
                    isValid = true
                )
            }
        }
    }

    fun deleteTag(tagId: Long) {
        viewModelScope.launch {
            storageRepository.deleteFoodTag(tagId)
        }
    }

    private fun getTagList() {
        viewModelScope.launch {
            storageRepository.getFoodTagFlow().collect { tagList ->
                foodTagList = tagList
            }
        }
    }
}