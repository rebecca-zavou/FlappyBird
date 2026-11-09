package com.example.flappybird.game

import kotlin.random.Random

class Game(val screenWidth: Float,
           val screenHeight: Float) {

    var bird = Bird(screenWidth / 4, screenHeight / 2)
    val pipes: MutableList<Pipe> = mutableListOf()
    var gameState : GameState = GameState.START
    var spawnTimer: Float = 0f
    var score = 0

    var bestScore = 0

    fun update(deltaTime: Float) {
        if (GameState.RUNNING == gameState) {
            bird.update(deltaTime)
            spawnTimer += deltaTime

            if (SPAWN_INTERVAL <= spawnTimer) {
                pipes.add(Pipe(screenWidth, randomGapTop()))
                spawnTimer -= SPAWN_INTERVAL
            }

            pipes.forEach { pipe -> pipe.update(deltaTime) }

            pipes.forEach { pipe ->
                if (!pipe.passed &&
                    pipe.x + PIPE_WIDTH < bird.x - BIRD_RADIUS) {
                    pipe.passed = true
                    score ++
                }
            }

            pipes.removeAll { pipe -> pipe.x <= -PIPE_WIDTH }

            val birdsTop = bird.y - BIRD_RADIUS
            val birdsBottom = bird.y + BIRD_RADIUS

            if (birdsTop <= 0f ||
                birdsBottom >= screenHeight ||
                pipes.any{ pipe ->
                    birdHits(pipe.x, pipe.x + PIPE_WIDTH, 0f, pipe.gapTop) ||
                    birdHits(pipe.x, pipe.x + PIPE_WIDTH, pipe.gapTop + GAP_SIZE, screenHeight)
                }) {

                gameState = GameState.GAME_OVER

                if (score > bestScore) {
                    bestScore = score
                }
            }
        }
    }

    fun onTap() {
        if (GameState.START == gameState) {
            gameState = GameState.RUNNING
            pipes.add(Pipe(screenWidth, randomGapTop()))
            bird.jump()
        } else if (GameState.RUNNING == gameState) {
            bird.jump()
        } else if (GameState.GAME_OVER == gameState) {
            resetGame()
        } else if (GameState.PAUSED == gameState) {
            gameState = GameState.RUNNING
            bird.jump()
        }
    }

    fun pause() {
        if (GameState.RUNNING == gameState) {
            gameState = GameState.PAUSED
        }
    }
    private fun randomGapTop() : Float {
        return MARGIN + Random.nextFloat() * (screenHeight - GAP_SIZE - 2*MARGIN)
    }

    private fun birdHits(left: Float,
                         right: Float,
                         top: Float,
                         bottom: Float) : Boolean {
        val birdsLeft = bird.x - BIRD_RADIUS
        val birdsRight = bird.x + BIRD_RADIUS
        val birdsTop = bird.y - BIRD_RADIUS
        val birdsBottom = bird.y + BIRD_RADIUS

        if (birdsLeft <= right &&
            birdsRight >= left &&
            birdsTop <= bottom &&
            birdsBottom >= top) {
            return true
        }

        return false
    }

    private fun resetGame() {
        bird = Bird(screenWidth / 4, screenHeight / 2)
        pipes.clear()
        spawnTimer = 0f
        gameState = GameState.START
        score = 0
    }
}