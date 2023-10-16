package hu.tb.minichefy.new_recipe

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateRecipeViewModel: ViewModel() {

    data class UiState(
        val activeField: String = "",
        val testList: List<String> = listOf("elso")
    )

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    fun onFieldChange(text: String){
        _state.value = _state.value.copy(
            activeField = text
        )
    }

    fun addRecipeStep(stepDescription: String){
        val updatedList = _state.value.testList.toMutableList().apply {
            add(stepDescription)
        }
        _state.value = state.value.copy(testList = updatedList)
        _state.value = state.value.copy(
            activeField = ""
        )
    }
}