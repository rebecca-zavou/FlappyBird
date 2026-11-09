package com.example.flappybird.game

// Sizes in pixels, speeds in pixels per second.
const val BIRD_RADIUS = 40f
const val GRAVITY = 100f          // how much the falling speed grows each second
const val JUMP_VELOCITY = -35f

const val PIPE_WIDTH = 150f
const val PIPE_SPEED = 300f
const val GAP_SIZE = 400f
const val MARGIN = 100f            // minimum pipe height above and below the gap
const val SPAWN_INTERVAL = 3.5f    // seconds between new pipes