package hu.tb.minichefy.new_recipe.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.R

@Composable
fun AddRemoveRow(
    onAddButtonClick: () -> Unit,
    onRemoveButtonClick: () -> Unit,
    displayContent: Int = 0
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onAddButtonClick) {
            Icon(painter = painterResource(id = R.drawable.baseline_add_24), contentDescription = "Add item")
        }
        Box(
            modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(text = displayContent.toString())
        }
        IconButton(onClick = onRemoveButtonClick) {
            Icon(painter = painterResource(id = R.drawable.baseline_remove_24), contentDescription = "Remove item")
        }
    }
}

@Preview
@Composable
fun AddRemoveRowPreview() {
    AddRemoveRow({}, {}, 5)
}