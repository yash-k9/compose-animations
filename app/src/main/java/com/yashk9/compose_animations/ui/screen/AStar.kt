package com.yashk9.compose_animations.ui.screen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yashk9.compose_animations.model.Spot
import com.yashk9.compose_animations.ui.components.GraphMap
import com.yashk9.compose_animations.ui.theme.ComposeanimationsTheme
import com.yashk9.compose_animations.util.AStarAlgo
import com.yashk9.compose_animations.util.Util.getScreenWidth
import com.yashk9.compose_animations.util.Util.setUpSpotMapList
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun AStar() {
    ComposeanimationsTheme {
        val algo = remember { AStarAlgo() }
        var isFindingPath by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val gridBoundary by remember { mutableStateOf(18) }
        var path by remember { mutableStateOf(mutableListOf<Spot>()) }
        var arraySpot by remember { mutableStateOf(mutableListOf(mutableListOf<Spot>())) }

        val onUpdateArraySpots: (MutableList<MutableList<Spot>>) -> Unit = {
            arraySpot = mutableListOf()
            arraySpot = it
        }

        val onUpdateSpotPath: (MutableList<Spot>) -> Unit = {
            path = mutableListOf()
            path = it
        }

        val onComplete: () -> Unit = {
            isFindingPath = false
        }

        setUpSpotMapList(gridBoundary, onUpdateSpotPath, onUpdateArraySpots)

        Scaffold(
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopAppBar(
                    title = { Text("A* Pathfinding Algo") },
                )
            },
            bottomBar = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Button(modifier = Modifier.padding(10.dp), onClick = {
                        if (!isFindingPath) {
                            setUpSpotMapList(gridBoundary, onUpdateSpotPath, onUpdateArraySpots)
                        }
                    }) {
                        Text("Generate Map")
                    }
                    Button(
                        onClick = {
                            scope.launch {
                                if (!isFindingPath) {
                                    isFindingPath = true
                                    algo.aStarPath(
                                        gridBoundary,
                                        arraySpot,
                                        onUpdateSpotPath,
                                        onComplete
                                    )
                                }
                        }
                    }) {
                        Text("Find Path")
                    }
                }
            }) {

            GraphMap(
                canvasSize = getScreenWidth().toFloat(),
                gridBoundary = gridBoundary,
                spotArray = arraySpot,
                path = path,
                modifier = Modifier
                    .fillMaxHeight(0.7f)
            )
        }
    }
}