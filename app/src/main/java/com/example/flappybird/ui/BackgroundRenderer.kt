package com.example.flappybird.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import com.example.flappybird.game.GameState
import kotlin.random.Random

/** Draws the sky gradient, drifting clouds and distant hills. */
class BackgroundRenderer {

    private var width = 0f
    private var height = 0f

    private val skyPaint = Paint()   // its gradient is set in resize(), once the height is known

    private val hillPaint = Paint().apply {
        color = Color.rgb(140, 210, 160)
        isAntiAlias = true
    }

    private val cloudPaint = Paint().apply {
        color = Color.WHITE
        alpha = 230                  // very slightly see-through
        isAntiAlias = true
    }

    private val clouds = mutableListOf<Cloud>()
    private val cloudSpeed = 30f     // pixels per second (pipes move at 300)

    fun resize(w: Float, h: Float) {
        width = w
        height = h

        // Sky: top-to-bottom gradient over the full height
        skyPaint.shader = LinearGradient(
            0f, 0f, 0f, h,
            Color.rgb(70, 170, 225),     // top: deeper blue
            Color.rgb(205, 240, 250),    // bottom: pale horizon
            Shader.TileMode.CLAMP
        )

        // Clouds: 5 at random positions in the top 40% of the screen
        clouds.clear()
        repeat(5) {
            clouds.add(
                Cloud(
                    x = Random.nextFloat() * w,
                    y = randomCloudY(),
                    size = 50f + Random.nextFloat() * 50f
                )
            )
        }
    }

    fun draw(canvas: Canvas, state: GameState, deltaTime: Float) {
        // Sky
        canvas.drawRect(0f, 0f, width, height, skyPaint)

        // Clouds drift only on the start screen and while playing
        val moving = state == GameState.START || state == GameState.RUNNING
        clouds.forEach { cloud ->
            if (moving) {
                cloud.x -= cloudSpeed * deltaTime
            }
            // Once a cloud has fully left on the left, bring it back on the right at a new height
            if (cloud.x + cloud.size * 2.2f < 0f) {
                cloud.x = width + cloud.size * 0.6f
                cloud.y = randomCloudY()
            }
            drawCloud(canvas, cloud)
        }

        // Hills: big circles centered below the screen, so only their tops show
        canvas.drawCircle(width * 0.2f, height * 1.05f, width * 0.35f, hillPaint)
        canvas.drawCircle(width * 0.65f, height * 1.10f, width * 0.45f, hillPaint)
        canvas.drawCircle(width * 1.0f, height * 1.03f, width * 0.30f, hillPaint)
    }

    private fun randomCloudY() = 150f + Random.nextFloat() * height * 0.4f

    private fun drawCloud(canvas: Canvas, cloud: Cloud) {
        val s = cloud.size
        canvas.drawCircle(cloud.x, cloud.y, s * 0.6f, cloudPaint)
        canvas.drawCircle(cloud.x + s * 0.7f, cloud.y - s * 0.3f, s * 0.75f, cloudPaint)
        canvas.drawCircle(cloud.x + s * 1.5f, cloud.y, s * 0.6f, cloudPaint)

        canvas.drawRoundRect(
            cloud.x - s * 0.2f, cloud.y - s * 0.2f,
            cloud.x + s * 1.7f, cloud.y + s * 0.6f,
            s * 0.4f, s * 0.4f, cloudPaint
        )
    }
}

private class Cloud(var x: Float, var y: Float, val size: Float)