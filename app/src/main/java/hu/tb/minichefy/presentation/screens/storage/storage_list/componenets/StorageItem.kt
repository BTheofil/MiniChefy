package hu.tb.minichefy.presentation.screens.storage.storage_list.componenets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.R
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.presentation.screens.components.icons.iconVectorResource
import hu.tb.minichefy.presentation.ui.theme.Green500
import hu.tb.minichefy.presentation.ui.theme.Red400
import hu.tb.minichefy.presentation.ui.theme.SMALL_SPACE_BETWEEN_ELEMENTS

@Composable
fun StorageItem(
    food: Food,
    onAddClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
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
                text = food.title.toUpperCase(Locale.current),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onRemoveClick) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_remove_24),
                        contentDescription = "remove quantity icon",
                        colorFilter = ColorFilter.tint(Red400)
                    )
                }
                Spacer(modifier = Modifier.width(SMALL_SPACE_BETWEEN_ELEMENTS))
                Text(
                    text = food.quantity.toString() + " " + food.unitOfMeasurement,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.width(SMALL_SPACE_BETWEEN_ELEMENTS))
                IconButton(onClick = onAddClick) {
                    Image(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "add quantity icon",
                        colorFilter = ColorFilter.tint(Green500)
                    )
                }
            }

        }
    }
}

@Preview
@Composable
fun StorageItemPreview() {
    StorageItem(
        food = Food(
            title = "apple",
            quantity = 5,
            unitOfMeasurement = UnitOfMeasurement.KG,
            type = ""
        ),
        onAddClick = {},
        onRemoveClick = {}
    )
}