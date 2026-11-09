package com.example.flappybird.ui

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import com.example.flappybird.game.Game
import com.example.flappybird.game.GameState

/**
 * Connects the game to Android: runs the frame loop, measures delta time,
 * routes taps, saves the best score, and asks each renderer to draw its part.
 */
class GameView(context: Context) : View(context) {

    private lateinit var game: Game
    private var lastTime: Long = 0L

    private val prefs = context.getSharedPreferences("flappy", Context.MODE_PRIVATE)
    private var savedBest = 0

    private val background = BackgroundRenderer()
    private val pipeRenderer = PipeRenderer()
    private val birdRenderer = BirdRenderer()
    private val hud = HudRenderer()
    private val pauseButton = PauseButton()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        game = Game(w.toFloat(), h.toFloat())

        savedBest = prefs.getInt("best", 0)
        game.bestScore = savedBest

        background.resize(w.toFloat(), h.toFloat())
        pauseButton.resize(w.toFloat())

        lastTime = System.nanoTime()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Timing and game update
        val now = System.nanoTime()
        val deltaTime = (now - lastTime).toFloat() / 1000000000f
        lastTime = now
        game.update(deltaTime)

        // Save a new best score
        if (game.bestScore > savedBest) {
            savedBest = game.bestScore
            prefs.edit().putInt("best", savedBest).apply()
        }

        // Drawing, back to front
        background.draw(canvas, game.gameState, deltaTime)
        pipeRenderer.draw(canvas, game.pipes, game.screenHeight)
        birdRenderer.draw(canvas, game.bird, game.gameState, deltaTime)
        if (game.gameState == GameState.RUNNING) {
            pauseButton.draw(canvas)
        }
        hud.draw(canvas, game, deltaTime)

        postInvalidateOnAnimation()
    }

    fun pause() {
        if (::game.isInitialized) {
            game.pause()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (game.gameState == GameState.RUNNING && pauseButton.contains(event.x, event.y)) {
                game.pause()
            } else {
                game.onTap()
            }
        }
        return true
    }
}