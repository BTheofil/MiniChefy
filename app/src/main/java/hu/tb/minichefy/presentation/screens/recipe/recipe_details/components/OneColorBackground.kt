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
import hu.tb.minichefy.presentation.ui.theme.Pink50

@Composable
fun OneColorBackground() {
    Canvas(Modifier.fillMaxSize()) {
        val cornerRadius = CornerRadius(64f, 64f)
        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(
                        offset = Offset(0f, 0f),
                        size = Size(size.width, size.height / 3f),
                    ),
                    bottomLeft = cornerRadius,
                    bottomRight = cornerRadius,
                )
            )
        }
        drawPath(path, color = Color(Pink50.value))
    }
}