 package com.yashk9.compose_animations.ui.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import com.yashk9.compose_animations.model.Spot

 @Composable
fun GraphMap(
     canvasSize: Float,
     gridBoundary: Int,
     spotArray: MutableList<MutableList<Spot>>,
     modifier: Modifier = Modifier,
     path: MutableList<Spot>
) {
    val rectSize = canvasSize / gridBoundary

    Canvas(
        modifier
            .fillMaxWidth()
            .clipToBounds()
            .background(MaterialTheme.colors.background)
    ) {
        val marginTop = size.height - size.width
        for (spot in spotArray.flatten()) {
            if (spot.isObstacle) {
                drawRect(
                    color = Color.Blue ,
                    topLeft = Offset(
                        (rectSize * spot.i),
                        (rectSize * spot.j + marginTop)
                    ),
                    size = Size(width = rectSize - 2, height = rectSize - 2),
                )
            } else {
                drawRect(
                    color = Color.Blue,
                    topLeft = Offset(
                        (rectSize * spot.i),
                        (rectSize * spot.j + marginTop)
                    ),
                    size = Size(width = rectSize - 2, height = rectSize - 2),
                    style = Stroke(0.5f)
                )
            }
        }

        for(p in path){
            drawRect(
                color = Color.Red,
                topLeft = Offset(
                    (rectSize * p.i),
                    (rectSize * p.j + marginTop)
                ),
                size = Size(width = rectSize - 2, height = rectSize - 2),
            )
        }
    }
}