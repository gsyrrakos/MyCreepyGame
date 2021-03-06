package com.github.nthily.flappybird.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp


class GameObject {

    val jumpDistance = 100f

    var screenHeight by mutableStateOf(0.dp)
    var screenWidth by mutableStateOf(0.dp)

    var requestAdd by mutableStateOf(false)
}