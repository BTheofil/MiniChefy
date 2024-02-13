package hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.presentation.screens.components.icons.iconVectorResource
import hu.tb.minichefy.presentation.ui.components.clickableWithoutRipple

@Composable
fun CircleImage(
    modifier: Modifier = Modifier,
    image: Int,
    borderWidth: Dp = 2.dp,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    borderShape: Shape = CircleShape,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = modifier
                .border(borderWidth,borderColor, borderShape)
                .padding(22.dp)
                .clickableWithoutRipple { onClick() },
            imageVector = iconVectorResource(iconResource = image),
            contentDescription = "Default icon"
        )
    }
}