package hu.tb.minichefy.presentation.screens.recipe.recipe_create.components.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import hu.tb.minichefy.R

@Composable
fun AddRemoveRow(
    onAddButtonClick: () -> Unit,
    onRemoveButtonClick: () -> Unit,
    displayContent: Int = 0,
    isErrorHappened: Boolean
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val (addRemoveRow, errorIcon) = createRefs()
        Row(
            modifier = Modifier
                .constrainAs(addRemoveRow, constrainBlock = {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = MaterialTheme.colorScheme.secondary),
                onClick = onRemoveButtonClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_remove_24),
                    contentDescription = "Remove item",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
            Spacer(modifier = Modifier.width(64.dp))
            Text(
                text = displayContent.toString(),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.width(64.dp))
            IconButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = MaterialTheme.colorScheme.primary),
                onClick = onAddButtonClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = "Add item",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        if (isErrorHappened)
            Icon(
                modifier = Modifier
                    .constrainAs(errorIcon, constrainBlock = {
                        start.linkTo(addRemoveRow.end)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }),
                imageVector = Icons.Rounded.Warning, contentDescription = "Warning sign icon",
                tint = MaterialTheme.colorScheme.error
            )
    }
}

@Preview
@Composable
fun AddRemoveRowPreview() {
    AddRemoveRow({}, {}, 5, true)
}