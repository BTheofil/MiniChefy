package hu.tb.minichefy.presentation.screens.recipe.recipe_details.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import hu.tb.minichefy.presentation.ui.theme.primaryContainerLight

@Composable
fun OneColorBackground(
    heightSize: Float = 1f,
    topLeftCorner: CornerRadius = CornerRadius.Zero,
    topRightCorner: CornerRadius = CornerRadius.Zero,
    bottomLeft: CornerRadius = CornerRadius.Zero,
    bottomRight: CornerRadius = CornerRadius.Zero,
) {
    Canvas(
        Modifier
            .fillMaxSize()
    ) {
        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(
                        offset = Offset(0f, 0f),
                        size = Size(size.width, size.height / heightSize),
                    ),
                    bottomLeft = bottomLeft,
                    bottomRight = bottomRight,
                    topLeft = topLeftCorner,
                    topRight = topRightCorner
                )
            )
        }
        drawPath(path, color = Color(primaryContainerLight.value))
    }
}

@Preview
@Composable
fun OneColorBackgroundPreview() {
    OneColorBackground()
}