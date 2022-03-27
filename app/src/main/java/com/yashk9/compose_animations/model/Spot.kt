package com.yashk9.compose_animations.model

data class Spot(
    var i: Int,
    var j: Int,
    var f: Int = 0,
    var g: Int = 0,
    var h: Int = 0,
    var neighbors: MutableList<Spot> = mutableListOf(),
    var previous: Spot? = null,
    var isObstacle: Boolean = false
){
    fun addNeighbors(items: MutableList<MutableList<Spot>>, num: Int) {
        if(i < num - 1){
            neighbors.add(items[i+1][j])
        }
        if(i > 0){
            neighbors.add(items[i-1][j])
        }
        if(j < num-1){
            neighbors.add(items[i][j+1])
        }
        if(j > 0){
            neighbors.add(items[i][j-1])
        }
    }
}