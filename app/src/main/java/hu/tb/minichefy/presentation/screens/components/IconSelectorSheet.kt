package hu.tb.minichefy.presentation.screens.components

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.domain.model.IconResource
import hu.tb.minichefy.presentation.util.icons.AppWideIcon
import hu.tb.minichefy.presentation.util.icons.MealIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconSelectorSheet(
    allIconList: List<IconResource.DrawableIcon>,
    selectedIcon: IconResource?,
    onItemClick: (icon: IconResource) -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                onItemClick(IconResource.GalleryIconImpl(it))
            }
        }
    )

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismissRequest
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = allIconList
            ) { foodIcon ->
                IconSelectorButton(
                    modifier = Modifier.size(110.dp),
                    foodIcon = foodIcon,
                    isSelected = foodIcon == selectedIcon,
                    onItemClick = { onItemClick(foodIcon) }
                )
            }
            item {
                IconSelectorButton(
                    modifier = Modifier.size(110.dp),
                    foodIcon = AppWideIcon.GALLERY,
                    onItemClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    isSelected = selectedIcon?.resource is Uri
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun IconSelectorButton(
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
    isSelected: Boolean = false,
    foodIcon: IconResource.DrawableIcon
) {
    ElevatedButton(
        modifier = modifier
            .padding(bottom = 8.dp),
        onClick = onItemClick,
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 3.dp
        ),
        border = if (isSelected) BorderStroke(8.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Image(
            modifier = Modifier
                .size(90.dp),
            imageVector = ImageVector.vectorResource(foodIcon.resource),
            contentDescription = "recipe food icon",
            contentScale = ContentScale.Fit
        )
    }
}

@Preview
@Composable
fun IconSelectorSheetPreview() {
    var isSheetVisible by remember {
        mutableStateOf(false)
    }
    var selectedIcon by remember {
        mutableStateOf<IconResource.DrawableIcon>(MealIcon.JUNK_FOOD)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        TextButton(onClick = {
            isSheetVisible = true
        }) {
            Text(text = "Show bottom sheet")
        }

        if (isSheetVisible) {
            IconSelectorSheet(
                allIconList = MealIcon.entries,
                selectedIcon = selectedIcon,
                onItemClick = { item ->
                    when (item) {
                        is IconResource.DrawableIcon -> selectedIcon = item
                        is IconResource.GalleryIcon -> {}
                    }
                },
                onDismissRequest = { isSheetVisible = false }
            )
        }
    }
}