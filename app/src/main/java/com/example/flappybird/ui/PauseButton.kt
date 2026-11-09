package com.example.flappybird.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

/** The pause icon in the top-right corner: knows where it is, how to draw itself, and whether a tap hit it. */
class PauseButton {

    private val size = 120f
    private val margin = 60f
    private val statusBarSpace = 60f   // keeps it below the status bar

    private var screenWidth = 0f

    private val paint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
    }

    fun resize(w: Float) {
        screenWidth = w
    }

    private fun left() = screenWidth - margin - size
    private fun top() = margin + statusBarSpace

    fun contains(x: Float, y: Float): Boolean {
        val left = left()
        val top = top()
        return x >= left && x <= left + size &&
                y >= top && y <= top + size
    }

    fun draw(canvas: Canvas) {
        val left = left()
        val top = top()
        val bottom = top + size
        // Two vertical bars: 25%–40% and 60%–75% across the button
        canvas.drawRect(left + size * 0.25f, top, left + size * 0.40f, bottom, paint)
        canvas.drawRect(left + size * 0.60f, top, left + size * 0.75f, bottom, paint)
    }
}