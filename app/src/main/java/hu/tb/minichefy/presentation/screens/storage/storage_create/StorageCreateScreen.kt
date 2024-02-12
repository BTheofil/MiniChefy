package hu.tb.minichefy.presentation.screens.storage.storage_create

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun StorageCreateScreen(
    viewModel: StorageCreateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StorageCreateContent(
        onBtnClick = {
            viewModel.onEvent(StorageCreateViewModel.OnEvent.BtnClick)
        }, {
            viewModel.onEvent(StorageCreateViewModel.OnEvent.OtherBtnClick)
        }
    )
}

@Composable
fun StorageCreateContent(
    onBtnClick: () -> Unit,
    onOtherBtnClick: () -> Unit
) {
    Column {
        Text(text = "create screen")
        Button(onClick = onBtnClick) {
            Text(text = "add milk")
        }
        Button(onClick = onOtherBtnClick) {
            Text(text = "add milk")
        }
    }
}

@Preview
@Composable
fun StorageCreateContentPreview() {
    StorageCreateContent({}, {})
}