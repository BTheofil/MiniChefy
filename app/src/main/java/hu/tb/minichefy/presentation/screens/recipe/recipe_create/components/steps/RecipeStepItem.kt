package hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.steps

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import hu.tb.minichefy.presentation.screens.components.extensions.clickableWithoutRipple

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecipeStepItem(
    index: Int,
    displayText: String,
    closeIconVisible: Boolean = true,
    isTextEditable: Boolean = true,
    keyboardController: SoftwareKeyboardController?,
    onRecipeStepTextFieldChange: (text: String) -> Unit,
    onDeleteItemClick: (Int) -> Unit = {},
    onRecipeItemClick: (Int) -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val (counterBox,
            textCard,
            closeIcon,
            line,
        ) = createRefs()

        val endGuideline = createGuidelineFromEnd(48.dp)

        Box(
            modifier = Modifier
                .constrainAs(counterBox, constrainBlock = {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                })
                .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .size(40.dp)
                .padding(12.dp)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = (index + 1).toString(),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }

        val primaryColorLine = MaterialTheme.colorScheme.primary
        Canvas(modifier = Modifier
            .constrainAs(line, constrainBlock = {
                top.linkTo(counterBox.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(textCard.start)
                height = Dimension.fillToConstraints
            }),
            onDraw = {
                drawLine(
                    color = primaryColorLine,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height * 1f),
                    strokeWidth = 12f
                )
            })

        ElevatedCard(
            modifier = Modifier
                .padding(bottom = 18.dp)
                .constrainAs(textCard, constrainBlock = {
                    start.linkTo(counterBox.end)
                    end.linkTo(endGuideline)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                })
                .padding(start = 16.dp)
                .clickableWithoutRipple { onRecipeItemClick(index) },
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.primary
                    .copy(alpha = 0.7f)
                    .compositeOver(Color.White),
            )
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(vertical = 16.dp, horizontal = 12.dp),
                value = displayText,
                onValueChange = onRecipeStepTextFieldChange,
                textStyle = MaterialTheme.typography.bodyMedium
                    .copy(color = MaterialTheme.colorScheme.onPrimary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }),
                enabled = isTextEditable
            )
        }

        Box(
            modifier = Modifier
                .constrainAs(closeIcon, constrainBlock = {
                    top.linkTo(parent.top)
                    start.linkTo(endGuideline)
                    end.linkTo(parent.end)
                })
                .clickableWithoutRipple {
                    onDeleteItemClick(index)
                },
            contentAlignment = Alignment.Center
        ) {
            if (closeIconVisible) {
                Icon(
                    modifier = Modifier,
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = "Delete step",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun RecipeStepItemPreview() {
    val keyboardController = LocalSoftwareKeyboardController.current
    RecipeStepItem(
        index = 1,
        displayText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer sed elit ligula. Sed pharetra luctus maximus. Donec vestibulum purus nec vestibulum bibendum. Nunc sapien ligula, dictum at lacus ut, tempus finibus lacus. Vivamus et augue ut quam rutrum sagittis ac at sem. Vivamus in libero ut nisi malesuada imperdiet.",
        onDeleteItemClick = {},
        onRecipeStepTextFieldChange = {},
        onRecipeItemClick = {},
        isTextEditable = true,
        keyboardController = keyboardController
    )
}