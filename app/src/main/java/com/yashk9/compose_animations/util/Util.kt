package com.yashk9.compose_animations.util

import android.graphics.Typeface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.yashk9.compose_animations.model.Card
import com.yashk9.compose_animations.model.Spot
import java.util.*
import kotlin.math.abs

object Util {
    /************************************* FlashCards ******************************************************/

    val flashCards = mutableListOf(
        Card(1, "Complexity of Merge Sort", "O(NlogN)"),
        Card(1, "Max Nodes in a Binary Tree", "(2^h) - 1"),
        Card(1, "Complexity of Quick Sort", "O(N^2)"),
        Card(1, "Inorder Traversal", "Left, Root, Right"),
        Card(1, "Stable Sorting Algo", "Merge Sort, Insertion Sort, Bubble Sort"),
        Card(1, "Flip nth bit", "num ^ (1 << n)"),
        Card(1, "XOR Operation", "Same bit returns 1 else 0"),
        Card(1, "Set nth bit", "num | (1 << n)"),
    )

    fun generateRandomColor(): Color {
        val rnd = Random()
        return Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256), 100)
    }

    /**************************************** AStar **************************************/

    fun setUpSpotMapList(size: Int,
                         onUpdateSpotPath: (MutableList<Spot>) -> Unit,
                         onUpdateArraySpots: (MutableList<MutableList<Spot>>) -> Unit
    ){
        val items: MutableList<MutableList<Spot>> = mutableListOf()
        for(i in 0 until size){
            items.add(mutableListOf())
            for(j in 0 until size){
                val random = kotlin.random.Random.nextDouble(0.0, 1.0)
                var isObstacle = false
                if(random < 0.25){
                    isObstacle = true
                }
                items[i].add(Spot(i, j, isObstacle = isObstacle))
            }
        }

        for(i in 0 until size){
            for(j in 0 until size){
                items[i][j].addNeighbors(items, size)
            }
        }

        items[0][0].isObstacle = false
        items[size-1][size-1].isObstacle = false

        onUpdateSpotPath(mutableListOf())
        onUpdateArraySpots(items)
    }

    @Composable
    fun getScreenWidth(): Int {
        val metrics = LocalContext.current.resources.displayMetrics
        return metrics.widthPixels
    }

    /************************************* Weather Animation ************************************/

    const val markersCount = 60
    const val markerAngle = 180/60

    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = 28.sp.value
        color = android.graphics.Color.WHITE
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
    }

    fun getSunriseTime(): Calendar = Calendar.getInstance().also {
        it.set(Calendar.HOUR_OF_DAY, 6)
        it.set(Calendar.MINUTE, 45)
    }

    fun getSunsetTime(): Calendar =  Calendar.getInstance().also{
        it.set(Calendar.HOUR_OF_DAY, 18)
        it.set(Calendar.MINUTE, 45)
    }

    fun getCurrHour() = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    fun getCurrAngle(curr: Int, sunrise: Calendar, sunset: Calendar) = when {
        curr <= sunrise.get(Calendar.HOUR_OF_DAY) -> {
            180f
        }
        curr >= sunset.get(Calendar.HOUR_OF_DAY) -> {
            0f
        }
        else -> {
            val currHour = sunrise.get(Calendar.HOUR_OF_DAY) - curr
            val currAngle = currHour * (180/12)
            180f - abs(currAngle).toFloat()
        }
    }

}