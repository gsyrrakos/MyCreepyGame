package com.github.nthily.flappybird.game


import androidx.compose.runtime.*

import androidx.compose.ui.unit.dp



enum class BirdState {
    Jumping, Falling
}

class Bird {

    var x by mutableStateOf(0f)
    var y by mutableStateOf(0f)
    var width by mutableStateOf(58.dp)
    var height by mutableStateOf(41.dp)

    fun jump(distance: Float){
        y -= distance
    }

}