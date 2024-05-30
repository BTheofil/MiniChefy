package hu.tb.minichefy.presentation.storage.create

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import hu.tb.minichefy.R
import hu.tb.minichefy.domain.model.IconResource
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

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

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

                        is StorageCreateViewModel.OnEvent.FoodUnitChange -> uiState.update {
                            it.copy(foodUnitOfMeasurement = event.type)
                        }

                        is StorageCreateViewModel.OnEvent.OnSelectedIconDialogClick -> TODO()
                        StorageCreateViewModel.OnEvent.Save -> {
                            val expectedFood = Food(
                                title = "Milk",
                                quantity = 1f,
                                unitOfMeasurement = UnitOfMeasurement.L,
                                icon = IconResource.DrawableIconImpl(1),
                                foodTagList = emptyList()
                            )

                            assertEquals(expectedFood.title, uiState.value.foodTitleText)
                            assertEquals(expectedFood.quantity, uiState.value.quantity.toFloat())
                            assertEquals(
                                expectedFood.unitOfMeasurement,
                                uiState.value.foodUnitOfMeasurement
                            )
                        }
                    }
                }
            )

        }

        rule.onNodeWithText(context.resources.getString(R.string.title)).performTextInput("Milk")
        rule.onNodeWithText(context.resources.getString(R.string.amount)).performTextInput("1")
        rule.onNodeWithTag("DropdownMenuIconTag").performClick()
        rule.onNodeWithText(context.resources.getString(R.string.l)).performClick()
        rule.onNodeWithText(context.resources.getString(R.string.save)).performClick()
    }
}