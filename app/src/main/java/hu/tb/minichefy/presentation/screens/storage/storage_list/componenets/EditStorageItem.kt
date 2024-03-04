package hu.tb.minichefy.presentation.screens.storage.storage_list.componenets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import hu.tb.minichefy.R
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.presentation.screens.components.icons.iconVectorResource
import hu.tb.minichefy.presentation.ui.components.clickableWithoutRipple
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS
import hu.tb.minichefy.presentation.ui.theme.SCREEN_VERTICAL_PADDING

@Composable
fun EditStorageItem(
    food: Food,
    onItemClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val (iconBox, content, closeIcon) = createRefs()

            Box(
                modifier = Modifier
                    .constrainAs(iconBox, constrainBlock = {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                        width = Dimension.value(80.dp)
                    })
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier,
                    imageVector = iconVectorResource(iconResource = R.drawable.junk_food_icon),
                    contentDescription = "Store icon"
                )
            }

            Column(
                modifier = Modifier
                    .constrainAs(content, constrainBlock = {
                        start.linkTo(iconBox.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    })
                    .padding(vertical = SCREEN_VERTICAL_PADDING),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = food.title.toUpperCase(Locale.current),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                food.foodTagList?.let { tags ->
                    repeat(tags.size) { index ->
                        AssistChip(
                            onClick = { /*TODO*/ },
                            label = { Text(text = tags[index].tag) },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Clear,
                                    contentDescription = ""
                                )
                            })
                    }
                }
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
                    .constrainAs(
                        closeIcon, constrainBlock = {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        }
                    ),
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    modifier = Modifier
                        .clickableWithoutRipple(onItemClick),
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
private fun EditStorageItemPreview() {
    EditStorageItem(
        food = Food(
            id = 0,
            title = "apple",
            quantity = 3f,
            unitOfMeasurement = UnitOfMeasurement.PIECE,
            foodTagList = listOf(FoodTag(0, "fruit"), FoodTag(2, "fruitfruitfruit"))
        ),
        onItemClick = {}
    )
}