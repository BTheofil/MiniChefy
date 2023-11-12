package hu.tb.minichefy.new_recipe.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RecipeStepItem(
    modifier: Modifier = Modifier,
    index: Int,
    item: String,
    onDeleteItemClick: (Int) -> Unit
) {
    ElevatedCard(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = (index + 1).toString() + ".",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier
                    .weight(1f),
                text = item,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(
                modifier = Modifier,
                onClick = {
                    onDeleteItemClick(index)

                }) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = "Delete step",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}