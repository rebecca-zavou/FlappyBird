package com.example.flappybird.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import com.example.flappybird.game.GAP_SIZE
import com.example.flappybird.game.PIPE_WIDTH
import com.example.flappybird.game.Pipe

/** Draws the pipes: shaded cylindrical bodies with wider, rounded caps at the gap. */
class PipeRenderer {

    private val capHeight = 50f
    private val capOverhang = 12f
    private val capCorner = 8f
    private val offScreen = 10f   // bodies extend past the screen edges so no outline shows there

    // Body: gradient across the pipe's width (x = 0 is the pipe's left edge)
    private val bodyPaint = Paint().apply {
        isAntiAlias = true
        shader = LinearGradient(
            0f, 0f, PIPE_WIDTH, 0f,
            intArrayOf(
                Color.rgb(70, 140, 40),    // left edge, darker
                Color.rgb(160, 225, 95),   // highlight
                Color.rgb(105, 190, 55),   // main green
                Color.rgb(55, 120, 30)     // right edge, shadow
            ),
            floatArrayOf(0f, 0.25f, 0.55f, 1f),
            Shader.TileMode.CLAMP
        )
    }

    // Cap: slightly lighter, spanning the wider cap
    private val capPaint = Paint().apply {
        isAntiAlias = true
        shader = LinearGradient(
            -capOverhang, 0f, PIPE_WIDTH + capOverhang, 0f,
            intArrayOf(
                Color.rgb(80, 155, 45),
                Color.rgb(175, 235, 110),
                Color.rgb(115, 200, 60),
                Color.rgb(60, 130, 35)
            ),
            floatArrayOf(0f, 0.25f, 0.55f, 1f),
            Shader.TileMode.CLAMP
        )
    }

    private val outlinePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 4f
        color = Color.rgb(40, 90, 20)
    }

    fun draw(canvas: Canvas, pipes: List<Pipe>, screenHeight: Float) {
        pipes.forEach { drawPipe(canvas, it, screenHeight) }
    }

    private fun drawPipe(canvas: Canvas, pipe: Pipe, screenHeight: Float) {
        val gapBottom = pipe.gapTop + GAP_SIZE

        canvas.save()
        canvas.translate(pipe.x, 0f)   // from here on, x = 0 is this pipe's left edge

        // Bodies
        drawOutlinedRect(canvas, 0f, -offScreen, PIPE_WIDTH, pipe.gapTop, bodyPaint)
        drawOutlinedRect(canvas, 0f, gapBottom, PIPE_WIDTH, screenHeight + offScreen, bodyPaint)

        // Caps (drawn after the bodies so they sit on top)
        drawOutlinedRect(canvas, -capOverhang, pipe.gapTop - capHeight,
            PIPE_WIDTH + capOverhang, pipe.gapTop, capPaint, capCorner)
        drawOutlinedRect(canvas, -capOverhang, gapBottom,
            PIPE_WIDTH + capOverhang, gapBottom + capHeight, capPaint, capCorner)

        canvas.restore()
    }

    private fun drawOutlinedRect(
        canvas: Canvas,
        left: Float, top: Float, right: Float, bottom: Float,
        fill: Paint,
        corner: Float = 0f
    ) {
        canvas.drawRoundRect(left, top, right, bottom, corner, corner, fill)
        canvas.drawRoundRect(left, top, right, bottom, corner, corner, outlinePaint)
    }
}