package hu.tb.minichefy.presentation.screens.recipe.recipe_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.domain.repository.RecipeRepository
import hu.tb.minichefy.domain.repository.StorageRepository
import hu.tb.minichefy.domain.use_case.DataStoreManager
import hu.tb.minichefy.presentation.screens.recipe.recipe_details.navigation.RECIPE_ID_ARGUMENT_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository,
    private val storageRepository: StorageRepository,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadRecipe()
        loadInformationDialogShouldShow()
    }

    data class UiState(
        val recipe: Recipe? = null,
        val isInformDialogShouldShow: Boolean = true
    )

    sealed class OnEvent {
        data object MakeRecipe : OnEvent()
        data class ShouldDialogAppear(val isDialogNeverShow: Boolean) : OnEvent()
    }

    fun onEvent(event: OnEvent) {
        when (event) {
            is OnEvent.MakeRecipe -> {
                viewModelScope.launch {
                    val dishTag = storageRepository.getTagById(3)
                    val isDishAlreadyInTheStorage =
                        storageRepository.searchExactlyOneFoodByTitle(uiState.value.recipe!!.title)

                    uiState.value.recipe.also { recipe ->
                        val savedDishId = storageRepository.saveOrModifyFood(
                            id = isDishAlreadyInTheStorage?.id,
                            icon = recipe!!.icon,
                            title = recipe.title,
                            quantity = recipe.quantity + (isDishAlreadyInTheStorage?.quantity
                                ?: 0f),
                            unitOfMeasurement = UnitOfMeasurement.PIECE,
                        )
                        storageRepository.saveFoodAndTag(savedDishId, dishTag.id!!)

                        //Log.i("RecipeDetailsVM", savedDishId.toString())
                    }
                }
            }

            is OnEvent.ShouldDialogAppear -> if (event.isDialogNeverShow) {
                viewModelScope.launch {
                    dataStoreManager.setNeverShowDialogInDetailsScreen()
                }
            }
        }
    }

    private fun loadRecipe() {
        val recipeId: String = checkNotNull(savedStateHandle[RECIPE_ID_ARGUMENT_KEY])
        viewModelScope.launch {
            val recipe = recipeRepository.getRecipeById(recipeId.toLong())

            _uiState.update {
                it.copy(
                    recipe = recipe
                )
            }
        }
    }

    private fun loadInformationDialogShouldShow() {
        viewModelScope.launch {
            dataStoreManager.isDialogShouldShowInDetailsScreen()
                .collect { isDialogShouldShow ->
                    _uiState.update {
                        it.copy(
                            isInformDialogShouldShow = isDialogShouldShow
                        )
                    }
                }
        }
    }
}