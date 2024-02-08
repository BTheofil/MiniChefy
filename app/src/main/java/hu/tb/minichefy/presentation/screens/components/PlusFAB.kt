package hu.tb.minichefy.presentation.screens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PlusFAB(
    onClick: () -> Unit
) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "fab with add icon"
        )
    }
}

@Preview
@Composable
fun PlusFABPreview() {
    PlusFAB {}
}