package de.mannodermaus.compose.tt.widgets

import androidx.compose.animation.animate
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.DragObserver
import androidx.compose.ui.gesture.dragGestureFilter
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun DraggableBox(
    modifier: Modifier = Modifier,
    initialPosition: Offset,
    content: @Composable (Boolean) -> Unit,
) {
    // State vars
    val offsetXState = remember { mutableStateOf(initialPosition.x) }
    val offsetYState = remember { mutableStateOf(initialPosition.y) }
    var dragging by remember { mutableStateOf(false) }

    // Animation
    val dragAnimSpec =
        remember { SpringSpec(stiffness = Spring.StiffnessHigh, visibilityThreshold = 0.1.dp) }
    val dropAnimSpec =
        remember { SpringSpec(stiffness = Spring.StiffnessLow, visibilityThreshold = 0.1.dp) }

    // Animated properties
    val density = DensityAmbient.current.density
    val animSpec = if (dragging) dragAnimSpec else dropAnimSpec
    val offsetX = animate((offsetXState.value / density).dp, animSpec = animSpec)
    val offsetY = animate((offsetYState.value / density).dp, animSpec = animSpec)

    Box(
        modifier = modifier
            .zIndex(if (dragging) 10f else 0f)
            .offset(offsetX, offsetY)
            .dragGestureFilter(object : DragObserver {
                override fun onStart(downPosition: Offset) {
                    dragging = true
                }

                override fun onDrag(dragDistance: Offset): Offset {
                    offsetXState.value += dragDistance.x
                    offsetYState.value += dragDistance.y
                    return dragDistance
                }

                override fun onStop(velocity: Offset) {
                    // todo snap to closest target or back to initial position
                    dragging = false
                    offsetXState.value = initialPosition.x
                    offsetYState.value = initialPosition.y
                }
            })
    ) {
        content(dragging)
    }
}

