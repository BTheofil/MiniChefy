package hu.tb.minichefy.presentation.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hu.tb.minichefy.presentation.ui.theme.SCREEN_HORIZONTAL_PADDING
import hu.tb.minichefy.presentation.ui.theme.SCREEN_VERTICAL_PADDING

@Composable
fun BaseListScreen(
    paddingValues: PaddingValues,
    isShowEmptyContent: Boolean,
    emptyContentDescriptionResource: Int,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(
                horizontal = SCREEN_HORIZONTAL_PADDING,
                vertical = SCREEN_VERTICAL_PADDING * 4
            )
    ) {
        when(isShowEmptyContent){
            true -> EmptyContent(descriptionResource = emptyContentDescriptionResource)
            false -> content()
        }
    }
}