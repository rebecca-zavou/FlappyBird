package com.example.flappybird.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.flappybird.game.Game
import com.example.flappybird.game.GameState

/** Draws everything on top of the game: score, state messages, and the white flash on a crash. */
class HudRenderer {

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 120f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val messagePaint = Paint().apply {
        color = Color.WHITE
        textSize = 70f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private var flashTimer = 0f
    private val flashDuration = 0.3f
    private var previousState = GameState.START

    fun draw(canvas: Canvas, game: Game, deltaTime: Float) {
        val centerX = game.screenWidth / 2f
        val centerY = game.screenHeight / 2f

        // Start the flash at the moment of a crash
        if (previousState == GameState.RUNNING && game.gameState == GameState.GAME_OVER) {
            flashTimer = flashDuration
        }
        previousState = game.gameState

        // Score
        canvas.drawText(game.score.toString(), centerX, 200f, textPaint)

        // Messages
        when (game.gameState) {
            GameState.START -> canvas.drawText("Tap to start", centerX, centerY + 200f, messagePaint)
            GameState.GAME_OVER -> {
                canvas.drawText("Game over", centerX, centerY, textPaint)
                canvas.drawText("Tap to restart", centerX, centerY + 120f, messagePaint)
                canvas.drawText("Best: " + game.bestScore, centerX, centerY + 240f, messagePaint)
            }
            GameState.PAUSED -> {
                canvas.drawText("Paused", centerX, centerY, textPaint)
                canvas.drawText("Tap to continue", centerX, centerY + 120f, messagePaint)
            }
            GameState.RUNNING -> {}
        }

        // Flash, drawn last so it covers everything
        if (flashTimer > 0f) {
            val a = (flashTimer / flashDuration * 255).toInt()
            canvas.drawColor(Color.argb(a, 255, 255, 255))
            flashTimer -= deltaTime
        }
    }
}