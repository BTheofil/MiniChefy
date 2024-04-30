package hu.tb.minichefy.presentation.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.repository.StorageRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val storageRepository: StorageRepository
): ViewModel() {

    var foodTagList by mutableStateOf<List<FoodTag>>(emptyList())
        private set

    var tagState by mutableStateOf("")
        private set

    init {
        getTagList()
    }

    fun updateTagTextFieldValue(text: String){
        tagState = text
    }

    fun saveNewTag(){
        viewModelScope.launch {
            storageRepository.saveOrModifyFoodTag(tag = FoodTag(tag = tagState))
            tagState = ""
        }
    }

    fun deleteTag(tagId: Long){
        viewModelScope.launch {
            storageRepository.deleteFoodById(tagId)
        }
    }

    private fun getTagList(){
        viewModelScope.launch {
            storageRepository.getFoodTagFlow().collect { tagList ->
                foodTagList = tagList
            }
        }
    }
}