package com.example.flappybird.game

class Pipe (
    var x: Float,
    val gapTop: Float
) {

    var passed = false
    fun update(deltaTime: Float) {
        x -= PIPE_SPEED * deltaTime
    }

}