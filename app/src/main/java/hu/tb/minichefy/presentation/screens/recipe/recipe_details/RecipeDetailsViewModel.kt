package hu.tb.minichefy.presentation.screens.recipe.recipe_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.recipe.SimpleQuickRecipeInfo
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.domain.repository.RecipeRepository
import hu.tb.minichefy.domain.repository.StorageRepository
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.navigation.RECIPE_ID_ARGUMENT_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {

    init {
        val recipeId: String = checkNotNull(savedStateHandle[RECIPE_ID_ARGUMENT_KEY])
        viewModelScope.launch {
            val recipe = recipeRepository.getRecipeById(recipeId.toLong())

            val quickInfoList = listOf(SimpleQuickRecipeInfo(recipe.quantity.toString(), "serve"))

            _uiState.update {
                it.copy(
                    recipe = recipe,
                    recipeQuickInfoList = quickInfoList
                )
            }
        }
    }

    data class UiState(
        val recipe: Recipe? = null,
        val recipeQuickInfoList: List<SimpleQuickRecipeInfo> = emptyList()
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    sealed class OnEvent {
        data object MakeRecipe : OnEvent()
    }

    fun onEvent(event: OnEvent) {
        when (event) {
            OnEvent.MakeRecipe -> {
                viewModelScope.launch {
                    uiState.value.recipe.also {
                        val savedProductId = storageRepository.saveOrModifyFood(
                            Food(
                                icon = it!!.icon,
                                title = it.title,
                                quantity = it.quantity.toFloat(),
                                unitOfMeasurement = UnitOfMeasurement.PIECE,
                                foodTagList = emptyList()
                            )
                        )
                        Log.i("RecipeDetailsVM", savedProductId.toString())
                    }
                }
            }
        }
    }
}