package hu.tb.minichefy.presentation.screens.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.presentation.ui.components.bottomBorder

@Composable
fun QuestionRowAnswer(
    questionText: String,
    textFieldValue: String,
    onTextFieldValueChange: (text: String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$questionText ",
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
            value = textFieldValue,
            onValueChange = onTextFieldValueChange,
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = MaterialTheme.colorScheme.secondary
            )
        )
    }
}

@Preview
@Composable
fun QuestionRowAnswerPreview() {
    QuestionRowAnswer(
        questionText = "Recipe title:",
        textFieldValue = "",
        onTextFieldValueChange = {}
    )
}