package com.yashk9.compose_animations.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yashk9.compose_animations.model.Marker
import com.yashk9.compose_animations.ui.theme.BlueBg
import com.yashk9.compose_animations.ui.theme.ComposeanimationsTheme
import com.yashk9.compose_animations.ui.theme.Inactive
import com.yashk9.compose_animations.ui.theme.SunYellow
import com.yashk9.compose_animations.util.Util.getCurrAngle
import com.yashk9.compose_animations.util.Util.getCurrHour
import com.yashk9.compose_animations.util.Util.getSunriseTime
import com.yashk9.compose_animations.util.Util.getSunsetTime
import com.yashk9.compose_animations.util.Util.markerAngle
import com.yashk9.compose_animations.util.Util.markersCount
import com.yashk9.compose_animations.util.Util.textPaint
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WeatherAnimation() {
    ComposeanimationsTheme {
        Scaffold(topBar = {
            TopAppBar(
                backgroundColor = BlueBg,
                title = { Text("Weather Animation", color = Color.White) },
            )
        }) {
            // get Calendar Instance
            val sunrise = getSunriseTime()
            val sunset = getSunsetTime()
            val curr = getCurrHour()

            // calculate angle based on current hour
            val currAngle = getCurrAngle(curr, sunrise, sunset)

            var start by remember { mutableStateOf(false) }
            val sunAngle = remember { Animatable(180f) }

            val markerList = mutableListOf<Marker>()

            // load markerList
            for (i in 0..markersCount) {
                markerList.add(Marker(i, false).apply {
                    this.angle = (this.position * markerAngle).toFloat()
                })
            }

            LaunchedEffect(key1 = start) {
                sunAngle.animateTo(
                    targetValue = if (!start) 180f else currAngle,
                    animationSpec = tween(1700)
                )
            }

            Box(
                modifier = Modifier
                    .background(Color.Gray.copy(alpha = 0.3f))
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(BlueBg, RoundedCornerShape(16.dp))
                ) {
                    Canvas(
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()
                    ) {
                        translate(0f, size.height / 3) {
                            val radius = (center.x - 50f / 0.32f)
                            val longRadius = (center.x - 50f / 0.38f)
                            val avgRadius = (radius + longRadius) / 2
                            val cx = center.x
                            val cy = center.y

                            //Draw Horizon
                            drawLine(
                                start = Offset(x = 0f + 50f, y = size.height / 2),
                                end = Offset(x = size.width - 50f, y = size.height / 2),
                                color = SunYellow
                            )

                            //Draw Start Circle
                            drawCircle(
                                SunYellow,
                                10f,
                                Offset(x = cx - avgRadius, y = cy)
                            )

                            //Draw End Circle
                            drawCircle(
                                SunYellow,
                                10f,
                                Offset(x = cx + avgRadius, y = cy)
                            )

                            //Text 1
                            drawText(
                                "6.45AM",
                                cx - avgRadius - 40f,
                                cy + 70f
                            )

                            //Text 2
                            drawText(
                                "6.45PM",
                                cx + avgRadius - 40f,
                                cy + 70f
                            )

                            //Draw Markers
                            drawMarkers(markerList, radius, longRadius)

                            //Draw Sun
                            drawSun(
                                calculateOffset(
                                    sunAngle.value,
                                    avgRadius, center,
                                    markerList,
                                    start
                                )
                            )
                        }
                    }
                }

                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = BlueBg,
                        contentColor = Color.White
                    ),
                    onClick = {
                        start = !start
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(40.dp)
                ) {
                    Text(text = if (start) "Reset" else "Start")
                }
            }
        }
    }
}

private fun calculateOffset(
    sunAngle: Float,
    radius: Float,
    center: Offset,
    markerList: MutableList<Marker>,
    start: Boolean,
): Offset {
    val angle =  Math.toRadians(sunAngle.toDouble() + 90f)
    val currMax = 180 - sunAngle

    markerList.forEach {
        if(180 - it.angle <= currMax){
            it.isActive = start
        }
    }

    val startX = (center.x + sin(angle) * radius)
    val startY = (center.y + cos(angle) * radius)

    return Offset(startX.toFloat(), startY.toFloat())
}



fun DrawScope.drawText(text: String, x: Float, y: Float){
    drawIntoCanvas {
        it.nativeCanvas.drawText(
            text,
            x,
            y,
            textPaint
        )
    }
}

fun DrawScope.drawMarkers(markerList: List<Marker>,
                          radius: Float,
                          longRadius: Float){
    val cx = center.x
    val cy = center.y

    for(marker in markerList){
        val angle = Math.toRadians((marker.angle + 90f).toDouble())

        val startX = (cx + sin(angle) * radius)
        val startY = (cy + cos(angle) * radius)

        val stopX = (cx + sin(angle) * longRadius)
        val stopY = (cy + cos(angle) * longRadius)

        drawLine(color = if(marker.isActive) SunYellow else Inactive,
            start = Offset(startX.toFloat(), startY.toFloat()),
            end = Offset(stopX.toFloat(), stopY.toFloat()),
        )
    }
}

fun DrawScope.drawSun(offset: Offset){
    val markerAngle = 360/8
    val radius = 20f
    val longRadius = 25f

    drawCircle(
        SunYellow,
        15f,
        offset,
        style = Stroke(width = 5f)
    )

    for(i in 0..360 step markerAngle){
        val currAngle =  Math.toRadians(i.toDouble())

        val startX = (offset.x + sin(currAngle) * radius)
        val startY = (offset.y + cos(currAngle) * radius)
        val stopX = (offset.x + sin(currAngle) * longRadius)
        val stopY = (offset.y + cos(currAngle) * longRadius)

        drawLine(
            SunYellow,
            start = Offset(startX.toFloat(), startY.toFloat()),
            end = Offset(stopX.toFloat(), stopY.toFloat()),
            strokeWidth = 10f,
            cap = StrokeCap.Round
        )
    }
}