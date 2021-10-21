package com.example.mycreepygame

import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.mycreepygame.ui.theme.MyCreepyGameTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.nthily.flappybird.Background
import com.github.nthily.flappybird.OverAlert
import com.github.nthily.flappybird.Score
import com.github.nthily.flappybird.UiState
import com.github.nthily.flappybird.game.BirdState
import com.github.nthily.flappybird.game.Game
import com.github.nthily.flappybird.game.GameState

import com.skydoves.landscapist.glide.GlideImage

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCreepyGameTheme {
                val viewModel = hiltViewModel<UiState>()
                val game by remember{ mutableStateOf(Game()) }
                game.restartGame()

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("FlappyBird")
                            },
                            actions = {
                                IconButton(onClick = { if(viewModel.type == 0) viewModel.type = 1  else viewModel.type = 0}) {
                                    Icon(painterResource(id = R.drawable.image), null)
                                }
                            },
                            backgroundColor = Color(0xFF000000)
                        )
                    }
                ){
                    GameUI(game)
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun GameUI(game: Game){


    val viewModel = hiltViewModel<UiState>()

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos {
                game.update(it)
            }
        }
    }


    val interactionSource = remember { MutableInteractionSource() }

    val unStartedAnimation by animateFloatAsState(
        targetValue = when(game.birdState){
            BirdState.Jumping -> 50f
            BirdState.Falling -> (-50f)
        },
        tween(500)
    )


    if(game.gameState == GameState.Unstarted){
        if(unStartedAnimation == -50f) {
            game.birdState = BirdState.Jumping
        } else if(unStartedAnimation == 50f)game.birdState = BirdState.Falling
    }


    val birdPosition by animateFloatAsState(game.creppybird.y,
        tween(
            when(game.gameState){
                GameState.Running -> if(game.birdState == BirdState.Falling) 150 else 50
                GameState.Over -> 1300
                GameState.Unstarted -> 0
            }, easing = LinearEasing
        )
    )

    Crossfade(targetState = viewModel.type) {
        if(it == 0){
            Background(R.drawable.bg)
        } else
            Background(R.drawable.bkg2)
    }

    // 柱子
    game.pipe.forEach{ pipe ->

        val pipeDownX by animateFloatAsState(pipe.pipeDownX, tween(150, easing = LinearEasing))
        val pipeUpX by animateFloatAsState(pipe.pipeUpX, tween(150, easing = LinearEasing))

        if(game.gameState == GameState.Running ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .offset(x = pipeDownX.dp),
                contentAlignment = Alignment.TopEnd
            ){

                GlideImage(
                    R.drawable.onlith,
                    contentDescription = null,
                    modifier = Modifier
                        .size(width = pipe.width, height = pipe.pipeDownHeight),
                    contentScale = ContentScale.FillBounds
                )


            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .offset(x = pipeUpX.dp),
                contentAlignment = Alignment.BottomEnd
            ){

                GlideImage(
                    R.drawable.onlith,
                    contentDescription = null,
                    modifier = Modifier
                        .size(width = pipe.width, height = pipe.pipeUpHeight),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                if (game.gameState != GameState.Over) {
                    game.gameState = GameState.Running
                    game.birdState = BirdState.Jumping
                }
            }
            .offset(
                y = when (game.gameState) {
                    GameState.Unstarted -> unStartedAnimation.dp
                    else -> birdPosition.dp
                }
            )
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                game.gameObject.screenHeight = placeable.measuredHeight.toDp()
                game.gameObject.screenWidth = placeable.measuredWidth.toDp()
                layout(placeable.width, placeable.height) {
                    placeable.placeRelative(0, 0)
                }
            },
        contentAlignment = Alignment.Center
    ){

        GlideImage(
            R.drawable.onlinet,
            contentDescription = null,
            modifier = Modifier
                .size(width = game.creppybird.width, height = game.creppybird.height)
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    game.creppybird.height = placeable.measuredHeight.toDp()
                    game.creppybird.width = placeable.measuredWidth.toDp()
                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(0, 0)
                    }
                },
            contentScale = ContentScale.FillBounds
        )

    }

    Score(game)
    OverAlert(game)

}