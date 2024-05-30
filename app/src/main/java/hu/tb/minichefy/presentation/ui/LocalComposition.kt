package hu.tb.minichefy.presentation.ui

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.staticCompositionLocalOf

val LocalModalDrawerState = staticCompositionLocalOf<DrawerState> { error("No DrawerState provided") }