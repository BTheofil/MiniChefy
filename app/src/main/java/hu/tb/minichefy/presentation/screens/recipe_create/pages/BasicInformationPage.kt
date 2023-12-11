package hu.tb.minichefy.presentation.screens.recipe_create.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.presentation.screens.recipe_create.components.AddRemoveRow
import hu.tb.minichefy.presentation.ui.components.bottomBorder

@Composable
fun BasicInformationPage(
    recipeName: String,
    counterDisplayContent: Int = 0,
    onTitleValueChange: (String) -> Unit,
    onRemoveQuantityClick: () -> Unit,
    onAddQuantityClick: () -> Unit,
    onNextPageClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp)
            .padding(top = 22.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recipe title: ",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .bottomBorder(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 1.dp
                    ),
                value = recipeName,
                onValueChange = onTitleValueChange,
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        }
        Spacer(
            modifier = Modifier
                .height(16.dp)
        )
        Text(
            text = "How many times can eat?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(
            modifier = Modifier
                .height(16.dp)
        )
        AddRemoveRow(
            onAddButtonClick = onAddQuantityClick,
            onRemoveButtonClick = onRemoveQuantityClick,
            displayContent = counterDisplayContent
        )
        Spacer(
            modifier = Modifier
                .height(16.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            OutlinedButton(
                modifier = Modifier,
                onClick = onNextPageClick
            ) {
                Text(
                    text = "Next page",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview
@Composable
fun BasicInformationPagePreview() {
    BasicInformationPage("test name", 0, {}, {}, {}, {})
}