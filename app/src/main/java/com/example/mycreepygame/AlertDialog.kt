package com.github.nthily.flappybird

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.github.nthily.flappybird.game.Game
import com.github.nthily.flappybird.game.GameState

@ExperimentalAnimationApi
@Composable

fun OverAlert(game: Game){



    if(game.gameState == GameState.Over){
        AlertDialog(
            onDismissRequest = {

            },
            title = {
                Text(
                    text = "Game Over",
                    fontWeight = FontWeight.W700,
                    style = MaterialTheme.typography.h6
                )
            },
            text = {
                Text(
                    text = "Your Score is ${game.score}",
                    style = MaterialTheme.typography.body1
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        game.restartGame()
                    },
                ) {
                    Text(
                        "Restart",
                        fontWeight = FontWeight.W700,
                        style = MaterialTheme.typography.button
                    )
                }
            },
        )

    }
}