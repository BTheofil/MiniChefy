package hu.tb.minichefy.presentation.storage.create

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.presentation.screens.storage.storage_create.StorageCreateContent
import hu.tb.minichefy.presentation.screens.storage.storage_create.StorageCreateViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class StorageCreateUITest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun check_food_creation() {
        val uiState = MutableStateFlow(StorageCreateViewModel.UiState())
        rule.setContent {
            StorageCreateContent(
                uiState = uiState.collectAsState().value,
                onEvent = { event ->
                    when (event) {
                        is StorageCreateViewModel.OnEvent.DialogChipClick -> TODO()
                        is StorageCreateViewModel.OnEvent.FoodQuantityChange -> uiState.update {
                            it.copy(quantity = event.quantityString)
                        }

                        is StorageCreateViewModel.OnEvent.FoodTextChange -> uiState.update {
                            it.copy(
                                foodTitleText = event.text
                            )
                        }

                        is StorageCreateViewModel.OnEvent.FoodTypeChange -> uiState.update {
                            it.copy(
                                foodType = event.type
                            )
                        }

                        is StorageCreateViewModel.OnEvent.FoodUnitChange -> uiState.update {
                            it.copy(foodUnitOfMeasurement = event.type)
                        }

                        is StorageCreateViewModel.OnEvent.OnSelectedIconDialogClick -> TODO()
                        StorageCreateViewModel.OnEvent.Save -> {
                            val expectedFood = Food(
                                title = "Alma",
                                quantity = 1f,
                                unitOfMeasurement = UnitOfMeasurement.L,
                                icon = 1,
                                foodTagList = emptyList()
                            )

                            assertEquals(expectedFood.title, uiState.value.foodTitleText)
                            assertEquals(expectedFood.quantity, uiState.value.quantity.toFloat())
                            assertEquals(expectedFood.unitOfMeasurement, uiState.value.foodUnitOfMeasurement)
                        }
                    }
                }
            )

        }

        rule.onNodeWithText("Title").performTextInput("Alma")
        rule.onNodeWithText("Solid").performClick()
        rule.onNodeWithText("Piece").performClick()
        rule.onNodeWithText("Liquid").performClick()
        rule.onNodeWithText("Amount").performTextInput("1")
        rule.onNodeWithTag("DropdownMenuIconTag").performClick()
        rule.onNodeWithText("L").performClick()
        rule.onNodeWithText("Save").performClick()
    }
}