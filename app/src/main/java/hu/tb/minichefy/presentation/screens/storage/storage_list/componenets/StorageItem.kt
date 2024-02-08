package hu.tb.minichefy.presentation.screens.storage.storage_list.componenets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.R
import hu.tb.minichefy.presentation.screens.recipe.components.iconVectorResource

@Composable
fun StorageItem() {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = iconVectorResource(iconResource = R.drawable.junk_food_icon),
                contentDescription = "Store icon"
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "title",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "5 left",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

        }
    }
}

@Preview
@Composable
fun StorageItemPreview() {
    StorageItem()
}