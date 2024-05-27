package hu.tb.minichefy.presentation.screens.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.presentation.screens.components.extensions.clickableWithoutRipple
import hu.tb.minichefy.presentation.ui.theme.CIRCLE_DRAWABLE_IMAGE_PADDING
import hu.tb.minichefy.presentation.ui.theme.CIRCLE_IMAGE_SIZE
import hu.tb.minichefy.domain.model.IconResource
import hu.tb.minichefy.presentation.util.icons.MealIcon

@Composable
fun CircleImage(
    image: IconResource,
    borderWidth: Dp = 2.dp,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    borderShape: Shape = CircleShape,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ImageWidget(
            modifier = Modifier
                .size(CIRCLE_IMAGE_SIZE)
                .clip(CircleShape)
                .border(borderWidth, borderColor, borderShape)
                .padding(if (image is IconResource.DrawableIcon) CIRCLE_DRAWABLE_IMAGE_PADDING else 0.dp)
                .clickableWithoutRipple { onClick() },
            image = image
        )
    }
}

@Preview
@Composable
private fun CircleImagePreview() {
    CircleImage(
        image = MealIcon.PIZZA
    )
}