package hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.steps

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import hu.tb.minichefy.presentation.screens.components.extensions.clickableWithoutRipple

@Composable
fun RecipeStepItem(
    modifier: Modifier = Modifier,
    index: Int,
    displayText: String,
    closeIconVisible: Boolean = true,
    isTextEditable: Boolean = true,
    keyboardController: SoftwareKeyboardController?,
    onRecipeStepTextFieldChange: (text: String) -> Unit,
    onDeleteItemClick: (Int) -> Unit = {},
    onRecipeItemClick: (Int) -> Unit,
    showContentAnimation: Transition<Boolean> = rememberTransition(
        transitionState = MutableTransitionState(
            false
        )
    ),
    showLineAnimation: Boolean = true,
) {
    val lineHeightAnimation = remember {
        Animatable(0f)
    }

    val alpha = showContentAnimation.animateFloat(
        label = "content appear/disappear animation",
        transitionSpec = {
            tween(durationMillis = 400, easing = LinearEasing)
        }
    ) { isVisible ->
        if (isVisible) 1f else 0f
    }

    LaunchedEffect(showLineAnimation) {
        lineHeightAnimation.animateTo(
            targetValue = if (showLineAnimation) 1f else 0f,
            animationSpec = tween(
                durationMillis = 600,
                easing = LinearEasing
            )
        )
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val (
            counterBox,
            textCard,
            closeIcon,
            line,
        ) = createRefs()

        val endGuideline = createGuidelineFromEnd(48.dp)

        Box(
            modifier = Modifier
                .graphicsLayer(alpha = alpha.value)
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
                    end = Offset(0f, size.height * lineHeightAnimation.value),
                    strokeWidth = 12f
                )
            })

        Card(
            modifier = Modifier
                .graphicsLayer(alpha = alpha.value)
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

@Preview
@Composable
fun RecipeStepItemPreview() {
    val keyboardController = LocalSoftwareKeyboardController.current
    var lineAnimation by remember {
        mutableStateOf(false)
    }
    var contentAnimation by remember {
        mutableStateOf(false)
    }

    Column {
        RecipeStepItem(
            index = 1,
            displayText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer sed elit ligula. Sed pharetra luctus maximus. Donec vestibulum purus nec vestibulum bibendum. Nunc sapien ligula, dictum at lacus ut, tempus finibus lacus. Vivamus et augue ut quam rutrum sagittis ac at sem. Vivamus in libero ut nisi malesuada imperdiet.",
            onDeleteItemClick = {},
            onRecipeStepTextFieldChange = {},
            onRecipeItemClick = {},
            isTextEditable = true,
            keyboardController = keyboardController,
            showLineAnimation = lineAnimation
        )
        Button(onClick = {
            lineAnimation = !lineAnimation
        }) {
            Text(text = "Start animation line")
        }
        Button(onClick = {
            contentAnimation = !contentAnimation
        }) {
            Text(text = "Start animation content")
        }
    }
}