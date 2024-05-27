package hu.tb.minichefy.presentation.screens.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import hu.tb.minichefy.presentation.util.icons.AppWideIcon
import hu.tb.minichefy.domain.model.IconResource
import hu.tb.minichefy.presentation.util.icons.iconVectorResource

@Composable
fun ImageWidget(
    modifier: Modifier = Modifier,
    image: IconResource
) {
    when (image) {
        is IconResource.GalleryIcon -> UriImage(
            modifier = modifier,
            imageUri = image.resource
        )
        is IconResource.DrawableIcon -> DrawableImage(
            modifier = modifier,
            imageResource = image.resource
        )
    }
}

@Composable
private fun UriImage(
    modifier: Modifier = Modifier,
    imageUri: Uri
) {
    AsyncImage(
        modifier = modifier,
        model = imageUri,
        contentDescription = "uri image",
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun DrawableImage(
    modifier: Modifier = Modifier,
    imageResource: Int
) {
    Image(
        modifier = modifier,
        imageVector = iconVectorResource(iconResource = imageResource),
        contentDescription = "drawable image"
    )
}

@Preview
@Composable
private fun IconWidgetPreview() {
    val testImage = AppWideIcon.DEFAULT_EMPTY_ICON

    ImageWidget(image = testImage)
}