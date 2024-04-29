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

    init {
        getTagList()
    }

    private fun getTagList(){
        viewModelScope.launch {
            storageRepository.getAllFoodTag().collect { tagList ->
                foodTagList = tagList
            }
        }
    }
}