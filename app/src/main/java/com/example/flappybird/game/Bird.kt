package com.example.flappybird.game


class Bird (
    val x: Float,
    var y: Float,
) {

    var velocity = 0f

    fun update(deltaTime: Float) {
        this.velocity += GRAVITY * deltaTime
        this.y += velocity
    }

    fun jump() {
        this.velocity = JUMP_VELOCITY
    }

}