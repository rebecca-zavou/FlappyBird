package com.example.flappybird.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.Shader
import com.example.flappybird.game.BIRD_RADIUS
import com.example.flappybird.game.Bird
import com.example.flappybird.game.GameState
import kotlin.math.sin

/** Draws the bird: shaded body, belly, flapping wing, eye and beak, tilted by its velocity. */
class BirdRenderer {

    private val r = BIRD_RADIUS

    private val bodyPaint = Paint().apply {
        isAntiAlias = true
        shader = RadialGradient(
            -0.3f * r, -0.35f * r, 1.4f * r,    // light comes from the top-left
            intArrayOf(
                Color.rgb(255, 240, 120),             // highlight
                Color.rgb(250, 200, 40),              // main yellow
                Color.rgb(230, 140, 20)               // shaded edge
            ),
            floatArrayOf(0f, 0.55f, 1f),
            Shader.TileMode.CLAMP
        )
    }

    private val outlinePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 4f
        color = Color.rgb(110, 60, 10)
    }

    private val bellyPaint = Paint().apply {
        isAntiAlias = true
        color = Color.rgb(255, 245, 200)
    }

    private val wingPaint = Paint().apply {
        isAntiAlias = true
        color = Color.rgb(245, 175, 35)
    }

    private val eyeWhitePaint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
    }

    private val pupilPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
    }

    private val beakPaint = Paint().apply {
        isAntiAlias = true
        color = Color.rgb(250, 110, 30)
    }

    // Triangle beak pointing right, around (0, 0) = the bird's center
    private val beakPath = Path().apply {
        moveTo(0.75f * r, -0.18f * r)
        lineTo(1.40f * r, 0.05f * r)
        lineTo(0.75f * r, 0.28f * r)
        close()
    }

    private var wingTime = 0f

    fun draw(canvas: Canvas, bird: Bird, state: GameState, deltaTime: Float) {
        // Wing flaps on the start screen and while playing; freezes when paused or crashed
        if (state == GameState.START || state == GameState.RUNNING) {
            wingTime += deltaTime
        }
        val wingAngle = sin(wingTime * 18f) * 25f            // swings between -25° and +25°
        val tilt = (bird.velocity * 0.3f).coerceIn(-60f, 110f)

        canvas.save()
        canvas.translate(bird.x, bird.y)   // from here on, (0, 0) is the bird's center
        canvas.rotate(tilt)                // rotate around that center

        // Body
        canvas.drawCircle(0f, 0f, r, bodyPaint)
        canvas.drawCircle(0f, 0f, r, outlinePaint)

        // Belly
        canvas.drawOval(-0.15f * r, 0.15f * r, 0.75f * r, 0.80f * r, bellyPaint)

        // Wing, rotated around where it joins the body
        canvas.save()
        canvas.rotate(wingAngle, -0.15f * r, 0.05f * r)
        canvas.drawOval(-0.90f * r, -0.15f * r, 0.05f * r, 0.35f * r, wingPaint)
        canvas.drawOval(-0.90f * r, -0.15f * r, 0.05f * r, 0.35f * r, outlinePaint)
        canvas.restore()

        // Eye
        canvas.drawCircle(0.40f * r, -0.30f * r, 0.32f * r, eyeWhitePaint)
        canvas.drawCircle(0.40f * r, -0.30f * r, 0.32f * r, outlinePaint)
        canvas.drawCircle(0.52f * r, -0.30f * r, 0.14f * r, pupilPaint)

        // Beak
        canvas.drawPath(beakPath, beakPaint)
        canvas.drawPath(beakPath, outlinePaint)

        canvas.restore()
    }
}