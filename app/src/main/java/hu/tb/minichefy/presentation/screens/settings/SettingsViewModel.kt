package hu.tb.minichefy.presentation.screens.settings

import androidx.lifecycle.ViewModel
import hu.tb.minichefy.R
import hu.tb.minichefy.domain.model.settings.SettingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    data class UiState(
        val settingsMenuList: List<SettingItem> = listOf(
            SettingItem(
                "Tag",
                R.drawable.tag_icon
            ),
            SettingItem(
                "Theme",
                R.drawable.theme_icon
            )
        )
    )
}