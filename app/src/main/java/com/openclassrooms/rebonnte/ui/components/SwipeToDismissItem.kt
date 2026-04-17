package com.openclassrooms.rebonnte.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.rebonnte.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    onDelete: () -> Unit,
    onClick: () -> Unit,
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                showConfirmDialog = true
                false
            } else {
                false
            }
        }
    )

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = {
                showConfirmDialog = false
                scope.launch { dismissState.snapTo(SwipeToDismissBoxValue.Settled) }
            },
            title = { Text(text = stringResource(R.string.confirm_deletion)) },
            text = { Text(text = stringResource(R.string.are_you_sure_you_want_to_delete, title)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showConfirmDialog = false
                        scope.launch { dismissState.snapTo(SwipeToDismissBoxValue.Settled) }
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        scope.launch { dismissState.snapTo(SwipeToDismissBoxValue.Settled) }
                    }
                ) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                Color.Red.copy(alpha = 0.8f)
            } else {
                Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(DELETE_ICON_BORDER_SHAPE.dp))
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(DELETE_ICON_BOX_SIZE.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                        .clickable { showConfirmDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White
                    )
                }
            }
        },
        content = {
            RebonnteItem(
                title = title,
                subtitle = subtitle,
                onClick = onClick,
                modifier = modifier.fillMaxWidth()
            )
        }
    )
}

private const val DELETE_ICON_BOX_SIZE = 40
private const val DELETE_ICON_BORDER_SHAPE = 8

@Preview(showBackground = true)
@Composable
private fun SwipeableItemPreview() {
    MaterialTheme {
        SwipeableItem(
            title = "Swipe Me",
            subtitle = "Swipe to delete",
            onDelete = {},
            onClick = {}
        )
    }
}

