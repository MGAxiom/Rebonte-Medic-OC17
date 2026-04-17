package com.openclassrooms.rebonnte.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.openclassrooms.rebonnte.R
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private enum class DragAnchor { Resting, Revealed }

@Composable
fun SwipeableItem(
    title: String,
    subtitle: String? = null,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val revealWidthDp = REVEAL_DP_SIZE.dp
    val revealWidthPx = with(density) { revealWidthDp.toPx() }

    val draggableState = remember {
        AnchoredDraggableState(
            initialValue = DragAnchor.Resting,
            anchors = DraggableAnchors {
                DragAnchor.Resting at 0f
                DragAnchor.Revealed at -revealWidthPx
            },
            positionalThreshold = { distance -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            snapAnimationSpec = spring(stiffness = Spring.StiffnessMedium),
            decayAnimationSpec = exponentialDecay(),
        )
    }

    val flingBehavior = AnchoredDraggableDefaults.flingBehavior(
        state = draggableState,
        positionalThreshold = { distance -> distance * 0.5f },
    )

    val isSwiped = draggableState.currentValue == DragAnchor.Revealed
            || draggableState.targetValue == DragAnchor.Revealed

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = {
                showConfirmDialog = false
                scope.launch { draggableState.animateTo(DragAnchor.Resting) }
            },
            title = { Text(text = stringResource(R.string.confirm_deletion)) },
            text = { Text(text = stringResource(R.string.are_you_sure_you_want_to_delete, title)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showConfirmDialog = false
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        scope.launch { draggableState.animateTo(DragAnchor.Resting) }
                    }
                ) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(DELETE_ICON_BORDER_SHAPE.dp))
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .offset {
                    IntOffset(
                        x = draggableState.requireOffset().roundToInt(),
                        y = 0
                    )
                }
                .anchoredDraggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    flingBehavior = flingBehavior
                )
        ) {
            RebonnteItem(
                title = title,
                subtitle = subtitle,
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (isSwiped) {
            Box(
                modifier = Modifier.matchParentSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp)
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
        }
    }
}

private const val DELETE_ICON_BOX_SIZE = 40
private const val DELETE_ICON_BORDER_SHAPE = 8
private const val REVEAL_DP_SIZE = 64
