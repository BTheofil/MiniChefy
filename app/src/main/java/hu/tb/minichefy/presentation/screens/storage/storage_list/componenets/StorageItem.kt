package hu.tb.minichefy.presentation.screens.storage.storage_list.componenets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.R
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.presentation.screens.components.icons.iconVectorResource
import hu.tb.minichefy.presentation.ui.components.clickableWithoutRipple
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS
import hu.tb.minichefy.presentation.ui.theme.SCREEN_VERTICAL_PADDING

@Composable
fun StorageItem(
    food: Food,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f))
                    .padding(8.dp)
            ) {
                Image(
                    imageVector = iconVectorResource(iconResource = R.drawable.junk_food_icon),
                    contentDescription = "Store icon"
                )
            }
            Spacer(modifier = Modifier.width(MEDIUM_SPACE_BETWEEN_ELEMENTS))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = SCREEN_VERTICAL_PADDING),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = food.title.toUpperCase(Locale.current),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                Text(
                    modifier = Modifier,
                    text = food.quantity.toString() + " " + food.unitOfMeasurement,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    modifier = Modifier
                        .clickableWithoutRipple(onEditClick),
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = "add quantity icon",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
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
            quantity = 15f,
            unitOfMeasurement = UnitOfMeasurement.KG,
            foodTagList = null
        ),
        onEditClick = {},
    )
}