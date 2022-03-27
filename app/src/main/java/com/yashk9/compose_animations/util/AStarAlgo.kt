package com.yashk9.compose_animations.util

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.yashk9.compose_animations.model.Spot
import kotlinx.coroutines.delay
import kotlin.math.abs


class AStarAlgo(){
    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun aStarPath(
        n: Int,
        spotList: MutableList<MutableList<Spot>>,
        updateSpotPathList: (MutableList<Spot>) -> Unit,
        onComplete: () -> Unit
    ) {
        val comparator = Comparator<Spot> { p0, p1 -> p0.f - p1.f }
        val openSet = mutableListOf<Spot>()
        val closedSet: MutableList<Spot> = mutableListOf()

        val start = spotList[0][0]
        val end = spotList[n-1][n-1]
        val pathFound = false

        openSet.add(start)

        while(!pathFound){
            delay(30)
            if(openSet.size > 0){
                //find the spot with minimum 'f' value
                openSet.sortWith(comparator)
                val curr = openSet[0]
                openSet.removeAt(0)

                val currPath = mutableListOf<Spot>()

                //If the solution is found, draw path and return
                if(curr == end){
                    computePath(curr, currPath)
                    updateSpotPathList(currPath)
                    onComplete()
                    return
                }

                curr.let {
                    //Add it in the closed set so it is not processed again
                    closedSet.add(it)

                    //Explore the neighbors and add the value
                    for (neighbor in curr.neighbors) {
                        if (!closedSet.contains(neighbor) && !neighbor.isObstacle) {
                            val tempG = curr.g + 1

                            if (openSet.contains(neighbor)) {
                                if (tempG < neighbor.g) {
                                    neighbor.g = tempG
                                }
                            } else {
                                neighbor.g = tempG
                                openSet.add(neighbor)
                            }

                            neighbor.h = heuristic(neighbor, end).toInt()
                            neighbor.f = neighbor.g + neighbor.h
                            neighbor.previous = curr
                        }
                    }
                }

                computePath(curr, currPath)
                updateSpotPathList(currPath)

            }else{
                Log.d(TAG, "AStar: Path Not Found")
                onComplete()
                return
            }
        }
    }

    private fun heuristic(curr: Spot, end: Spot): Double {
        val dx = abs(curr.i - end.i).toDouble()
        val dy = abs(curr.j - end.j).toDouble()
        return dx + dy
    }

    private fun computePath(curr: Spot, path: MutableList<Spot>) {
        var temp = curr
        path.add(temp)
        while(temp.previous != null){
            path.add(temp.previous!!)
            temp = temp.previous!!
        }
    }

    companion object{
        private const val TAG = "AStar"
    }
}

