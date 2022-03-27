package com.yashk9.compose_animations.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yashk9.compose_animations.util.Util.generateRandomColor
import com.yashk9.compose_animations.model.Card
import kotlinx.coroutines.launch
import kotlin.math.abs

enum class SwipeResult {
    ACCEPTED, REJECTED
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableCard(
    card: Card,
    modifier: Modifier = Modifier,
    onSwiped: (Any, Any) -> Unit,
) {

    val color by remember { mutableStateOf(generateRandomColor()) }
    var isRotated by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isRotated) 180f else 0f,
        animationSpec = tween(400)
    )

    val alphaFront by animateFloatAsState(
        targetValue = if (!isRotated) 1f else 0f,
        animationSpec = tween(400)
    )

    val alphaReverse by animateFloatAsState(
        targetValue = if (isRotated) 1f else 0f,
        animationSpec = tween(400)
    )

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val swipeXLeft = -(screenWidth.value * 3.2).toFloat()
    val swipeXRight = (screenWidth.value * 3.2).toFloat()
    val swipeYTop = -1000f
    val swipeYBottom = 1000f
    val swipeX = remember { Animatable(0f) }
    val swipeY = remember { Animatable(0f) }
    swipeX.updateBounds(swipeXLeft, swipeXRight)
    swipeY.updateBounds(swipeYTop, swipeYBottom)

    if (abs(swipeX.value) < swipeXRight - 50f) {
        val rotationFraction = (swipeX.value / 20).coerceIn(-40f, 40f)
        Card(
            elevation = 16.dp,
            modifier = modifier
                .dragContent(
                    swipeX = swipeX,
                    swipeY = swipeY,
                    maxX = swipeXRight,
                    onSwiped = { _, _ -> }
                )
                .graphicsLayer {
                    translationX = swipeX.value
                    translationY = swipeY.value
                    rotationZ = rotationFraction
                    rotationY = rotation
                    cameraDistance = 9 * density
                }
                .clickable {
                    isRotated = !isRotated
                }
                .clip(RoundedCornerShape(16.dp)),
        ) {
            Column(
                modifier = Modifier.background(color),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(text = if (isRotated) card.answer else card.question,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = if (isRotated) alphaReverse else alphaFront
                            rotationY = rotation
                        })
            }
        }
    } else {
        // on swiped
        val swipeResult = if (swipeX.value > 0) SwipeResult.ACCEPTED else SwipeResult.REJECTED
        onSwiped(swipeResult, card)
    }
}


fun Modifier.dragContent(
    swipeX: Animatable<Float, AnimationVector1D>,
    swipeY: Animatable<Float, AnimationVector1D>,
    maxX: Float,
    onSwiped: (Any, Any) -> Unit
): Modifier = composed {
    val coroutineScope = rememberCoroutineScope()
    pointerInput(Unit) {
        this.detectDragGestures(
            onDragCancel = {
                coroutineScope.apply {
                    launch { swipeX.animateTo(0f) }
                    launch { swipeY.animateTo(0f) }
                }
            },
            onDragEnd = {
                coroutineScope.apply {
                    // if it's swiped 1/4th
                    if (abs(swipeX.targetValue) < abs(maxX) / 4) {
                        launch {
                            swipeX.animateTo(0f, tween(400))
                        }
                        launch {
                            swipeY.animateTo(0f, tween(400))
                        }
                    } else {
                        launch {
                            if (swipeX.targetValue > 0) {
                                swipeX.animateTo(maxX, tween(400))
                            } else {
                                swipeX.animateTo(-maxX, tween(400))
                            }
                        }
                    }
                }
            }
        ) { change, dragAmount ->
            change.consumePositionChange()
            coroutineScope.apply {
                launch { swipeX.animateTo(swipeX.targetValue + dragAmount.x) }
                launch { swipeY.animateTo(swipeY.targetValue + dragAmount.y) }
            }
        }
    }
}