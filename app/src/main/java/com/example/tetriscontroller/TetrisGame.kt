package com.example.tetriscontroller

import android.graphics.Color

class TetrisGame(val rows: Int = 20, val cols: Int = 10) {
    val grid = Array(rows) { IntArray(cols) }
    var currentBlock = generateRandomBlock()
    var isGameOver = false

    fun generateRandomBlock(): Tetromino {
        return Tetromino.random(cols)
    }

    fun moveLeft() {
        if (!isCollision(currentBlock.x - 1, currentBlock.y, currentBlock.shape)) {
            currentBlock.x--
        }
    }

    fun moveRight() {
        if (!isCollision(currentBlock.x + 1, currentBlock.y, currentBlock.shape)) {
            currentBlock.x++
        }
    }

    fun update() {
        if (isCollision(currentBlock.x, currentBlock.y + 1, currentBlock.shape)) {
            fixBlock()
            clearFullLines()
            currentBlock = generateRandomBlock()
            if (isCollision(currentBlock.x, currentBlock.y, currentBlock.shape)) {
                isGameOver = true
            }
        } else {
            currentBlock.y++
        }
    }

    private fun fixBlock() {
        val shape = currentBlock.shape
        for (r in shape.indices) {
            for (c in shape[r].indices) {
                if (shape[r][c] == 1) {
                    val gx = currentBlock.x + c
                    val gy = currentBlock.y + r
                    if (gy in 0 until rows && gx in 0 until cols) {
                        grid[gy][gx] = 1
                    }
                }
            }
        }
    }

    private fun clearFullLines() {
        val newGrid = mutableListOf<IntArray>()
        for (r in grid.indices) {
            if (grid[r].all { it == 1 }) {
                newGrid.add(0, IntArray(cols))
            } else {
                newGrid.add(grid[r])
            }
        }
        for (r in 0 until rows) {
            grid[r] = newGrid[r]
        }
    }

    fun isCollision(x: Int, y: Int, shape: Array<IntArray>): Boolean {
        for (r in shape.indices) {
            for (c in shape[r].indices) {
                if (shape[r][c] == 1) {
                    val newX = x + c
                    val newY = y + r
                    if (newX !in 0 until cols || newY >= rows) return true
                    if (newY >= 0 && grid[newY][newX] == 1) return true
                }
            }
        }
        return false
    }

    data class Tetromino(
        var x: Int,
        var y: Int,
        var shape: Array<IntArray>,
        val type: Type
    ) {
        enum class Type(val color: Int) {
            I(Color.RED),
            O(Color.YELLOW),
            T(Color.MAGENTA),
            S(Color.GREEN),
            Z(Color.rgb(255, 100, 100)),
            L(Color.rgb(255, 165, 0)),
            J(Color.BLUE)
        }

        companion object {
            fun random(cols: Int): Tetromino {
                val options = listOf(
                    Tetromino(0, 0, arrayOf(intArrayOf(1, 1, 1, 1)), Type.I),
                    Tetromino(0, 0, arrayOf(intArrayOf(1, 1), intArrayOf(1, 1)), Type.O),
                    Tetromino(0, 0, arrayOf(intArrayOf(0, 1, 0), intArrayOf(1, 1, 1)), Type.T),
                    Tetromino(0, 0, arrayOf(intArrayOf(0, 1, 1), intArrayOf(1, 1, 0)), Type.S),
                    Tetromino(0, 0, arrayOf(intArrayOf(1, 1, 0), intArrayOf(0, 1, 1)), Type.Z),
                    Tetromino(0, 0, arrayOf(intArrayOf(1, 0, 0), intArrayOf(1, 1, 1)), Type.L),
                    Tetromino(0, 0, arrayOf(intArrayOf(0, 0, 1), intArrayOf(1, 1, 1)), Type.J)
                )
                val block = options.random()
                val startX = (cols - block.shape[0].size) / 2
                block.x = startX
                block.y = 0
                return block
            }
        }

        fun rotate(game: TetrisGame) {
            val rotated = rotateMatrix(shape)
            if (!game.isCollision(x, y, rotated)) {
                shape = rotated
            }
        }

        private fun rotateMatrix(matrix: Array<IntArray>): Array<IntArray> {
            val rows = matrix.size
            val cols = matrix[0].size
            val rotated = Array(cols) { IntArray(rows) }
            for (r in matrix.indices) {
                for (c in matrix[r].indices) {
                    rotated[c][rows - 1 - r] = matrix[r][c]
                }
            }
            return rotated
        }
    }
}