package hu.tb.minichefy.presentation.screens.storage.storage_list.componenets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.tb.minichefy.presentation.ui.components.clickableWithoutRipple

@Composable
fun EditStorageItem(
    onItemClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickableWithoutRipple(onItemClick)
    ) {
        Text(text = "hello")
    }
}

@Preview
@Composable
private fun EditStorageItemPreview() {
    EditStorageItem(
        onItemClick = {}
    )
}