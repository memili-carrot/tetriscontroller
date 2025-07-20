package com.example.tetriscontroller

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class TetrisView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    val game = TetrisGame()
    private val paint = Paint()

    init {
        postDelayed(object : Runnable {
            override fun run() {
                if (!game.isGameOver) {
                    game.update()
                    invalidate()
                    postDelayed(this, 500)
                }
            }
        }, 500)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val blockSize = minOf(width / game.cols, height / game.rows)
        val offsetX = (width - blockSize * game.cols) / 2
        val offsetY = (height - blockSize * game.rows) / 2

        drawFixedBlocks(canvas, blockSize, offsetX, offsetY)
        drawCurrentBlock(canvas, blockSize, offsetX, offsetY)

        if (game.isGameOver) {
            paint.color = android.graphics.Color.RED
            paint.textSize = 80f
            canvas.drawText("GAME OVER", offsetX + 50f, height / 2f, paint)
        }
    }

    private fun drawFixedBlocks(canvas: Canvas, blockSize: Int, offsetX: Int, offsetY: Int) {
        for (r in 0 until game.rows) {
            for (c in 0 until game.cols) {
                if (game.grid[r][c] == 1) {
                    paint.color = android.graphics.Color.DKGRAY
                    canvas.drawRect(
                        offsetX + c * blockSize.toFloat(),
                        offsetY + r * blockSize.toFloat(),
                        offsetX + (c + 1) * blockSize.toFloat(),
                        offsetY + (r + 1) * blockSize.toFloat(),
                        paint
                    )
                }
            }
        }
    }

    private fun drawCurrentBlock(canvas: Canvas, blockSize: Int, offsetX: Int, offsetY: Int) {
        val block = game.currentBlock
        paint.color = block.type.color

        for (r in block.shape.indices) {
            for (c in block.shape[r].indices) {
                if (block.shape[r][c] == 1) {
                    val x = offsetX + (block.x + c) * blockSize
                    val y = offsetY + (block.y + r) * blockSize
                    canvas.drawRect(
                        x.toFloat(), y.toFloat(),
                        (x + blockSize).toFloat(), (y + blockSize).toFloat(), paint
                    )
                }
            }
        }
    }
}