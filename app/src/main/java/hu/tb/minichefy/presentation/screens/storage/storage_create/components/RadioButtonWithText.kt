package hu.tb.minichefy.presentation.screens.storage.storage_create.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.tb.minichefy.presentation.screens.components.extensions.clickableWithoutRipple

@Composable
fun RadioButtonWithText(
    modifier: Modifier = Modifier,
    displayText: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickableWithoutRipple {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = displayText,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
fun RadioButtonWithTextPreview() {
    RadioButtonWithText(
        displayText = "liquid",
        isSelected = true,
        onClick = {}
    )
}