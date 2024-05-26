package hu.tb.minichefy.presentation.screens.components

import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import hu.tb.minichefy.presentation.screens.components.extensions.clickableWithoutRipple
import hu.tb.minichefy.presentation.util.icons.MealIcon
import hu.tb.minichefy.presentation.util.icons.iconVectorResource

@Composable
fun CircleImage(
    image: Any,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        when (image) {
            is Uri -> UriImageWidget(image = image, onClick = onClick)
            is MealIcon -> MealImageWidget(imageResource = image, onClick = onClick)
        }
    }
}

@Composable
private fun UriImageWidget(
    modifier: Modifier = Modifier,
    image: Uri,
    borderWidth: Dp = 2.dp,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    borderShape: Shape = CircleShape,
    onClick: () -> Unit = {}
) {
    AsyncImage(
        modifier = modifier
            .size(124.dp)
            .clip(CircleShape)
            .border(borderWidth, borderColor, borderShape)
            .clickableWithoutRipple { onClick() },
        model = image,
        contentDescription = "recipe image",
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun MealImageWidget(
    modifier: Modifier = Modifier,
    imageResource: MealIcon,
    borderWidth: Dp = 2.dp,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    borderShape: Shape = CircleShape,
    onClick: () -> Unit = {}
) {
    Image(
        modifier = modifier
            .size(124.dp)
            .border(borderWidth, borderColor, borderShape)
            .padding(22.dp)
            .clickableWithoutRipple { onClick() },
        imageVector = iconVectorResource(iconResource = imageResource.resource),
        contentDescription = "Default icon"
    )
}

@Preview
@Composable
private fun CircleImagePreview() {
    CircleImage(
        image = MealIcon.PIZZA.resource
    )
}