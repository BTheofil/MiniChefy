package hu.tb.minichefy.presentation.ui

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.compositionLocalOf

val LocalModalDrawerState = compositionLocalOf<DrawerState> { error("No DrawerState provided") }