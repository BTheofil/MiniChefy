package hu.tb.minichefy.presentation.screens.storage.storage_list.componenets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import hu.tb.minichefy.R
import hu.tb.minichefy.domain.model.storage.Food
import hu.tb.minichefy.domain.model.storage.FoodTag
import hu.tb.minichefy.presentation.preview.FoodPreviewParameterProvider
import hu.tb.minichefy.presentation.screens.components.ImageWidget
import hu.tb.minichefy.presentation.screens.components.extensions.clickableWithoutRipple
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS
import hu.tb.minichefy.presentation.ui.theme.SMALL_SPACE_BETWEEN_ELEMENTS
import kotlin.math.min

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditStorageItem(
    food: Food,
    onFoodIconClick: () -> Unit,
    onCloseClick: () -> Unit,
    onAddTagClick: () -> Unit,
    onDeleteTagClick: (tag: FoodTag) -> Unit,
    onQuantityClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(
            contentColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
        )
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
                    .clickableWithoutRipple(onFoodIconClick)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                ImageWidget(image = food.icon)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(content, constrainBlock = {
                        start.linkTo(iconBox.end)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    })
                    .padding(vertical = 16.dp)
                    .padding(horizontal = MEDIUM_SPACE_BETWEEN_ELEMENTS),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = food.title.toUpperCase(Locale.current),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(MEDIUM_SPACE_BETWEEN_ELEMENTS))
                FlowRow(
                    maxItemsInEachRow = 5,
                    horizontalArrangement = Arrangement.spacedBy(SMALL_SPACE_BETWEEN_ELEMENTS),
                ) {
                    AssistChip(
                        onClick = onAddTagClick,
                        label = {
                            Icon(
                                painterResource(id = R.drawable.baseline_add_24),
                                contentDescription = "add more tag icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
                    )
                    food.foodTagList?.let { tags ->
                        repeat(min(tags.size, 3)) { index ->
                            AssistChip(
                                onClick = { onDeleteTagClick(tags[index]) },
                                label = {
                                    Text(
                                        text = tags[index].tag,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                },
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Clear,
                                        contentDescription = "Remove tag"
                                    )
                                })
                        }
                        if (tags.size > 3) {
                            AssistChip(
                                onClick = onAddTagClick,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                                label = {
                                    Text(
                                        text = "+" + (tags.size - 3),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                })
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        modifier = Modifier
                            .weight(1f),
                        onClick = onQuantityClick,
                        content = {
                            Text(
                                text = food.quantity.toString() + " " + food.unitOfMeasurement,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Center,
                            )
                        },
                    )
                }
            }

            Box(
                modifier = Modifier
                    .padding(top = 16.dp, end = 16.dp)
                    .constrainAs(
                        closeIcon, constrainBlock = {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        }
                    ),
                contentAlignment = Alignment.TopEnd
            ) {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .clickableWithoutRipple(onCloseClick),
                    imageVector = Icons.Outlined.KeyboardArrowUp,
                    contentDescription = "add quantity icon",
                    tint = Color(MaterialTheme.colorScheme.primary.value)
                )
            }
        }
    }
}

@Preview
@Composable
private fun EditStorageItemPreview(
    @PreviewParameter(FoodPreviewParameterProvider::class) foodList: List<Food>
) {
    EditStorageItem(
        food = foodList.first(),
        {}, {}, {}, {}, {}
    )
}