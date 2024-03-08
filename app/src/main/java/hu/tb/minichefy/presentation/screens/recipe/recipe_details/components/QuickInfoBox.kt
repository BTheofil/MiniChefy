package hu.tb.minichefy.presentation.screens.recipe.recipe_details.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.domain.model.recipe.SimpleQuickRecipeInfo
import hu.tb.minichefy.presentation.ui.theme.MEDIUM_SPACE_BETWEEN_ELEMENTS

@Composable
fun QuickInfoBox(
    infoList: List<SimpleQuickRecipeInfo>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .border(
                    1.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(MEDIUM_SPACE_BETWEEN_ELEMENTS)
            ) {
                items(items = infoList){
                    QuickInfoItem(
                        mainDataInfo = it.main,
                        detailsInfo = it.secondary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun QuickInfoBoxPreview() {
    val indoTestList = listOf(
        SimpleQuickRecipeInfo("20", "minutes"),
        SimpleQuickRecipeInfo("5", "serve"),
    )

    QuickInfoBox(indoTestList)
}