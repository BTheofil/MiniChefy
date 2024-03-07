package hu.tb.minichefy.presentation.screens.storage.storage_list.componenets

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import hu.tb.minichefy.presentation.ui.theme.SMALL_SPACE_BETWEEN_ELEMENTS

@Composable
fun StorageItem(
    food: Food,
    onEditClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .clickableWithoutRipple(onEditClick)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    imageVector = iconVectorResource(iconResource = R.drawable.junk_food_icon),
                    contentDescription = "Store icon"
                )
            }
            Spacer(modifier = Modifier.width(MEDIUM_SPACE_BETWEEN_ELEMENTS))
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = food.title.toUpperCase(Locale.current),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        modifier = Modifier
                            .size(32.dp),
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = "add quantity icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    modifier = Modifier,
                    text = food.quantity.toString() + " " + food.unitOfMeasurement,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MEDIUM_SPACE_BETWEEN_ELEMENTS)
                .padding(top = SMALL_SPACE_BETWEEN_ELEMENTS),
            thickness = 1.dp,
            color = Color.Gray
        )
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