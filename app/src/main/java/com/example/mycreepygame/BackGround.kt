package com.github.nthily.flappybird


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.skydoves.landscapist.glide.GlideImage



@Composable
fun Background(
    img: Int
){

    GlideImage(
       img,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
        contentDescription = ""
    )

}