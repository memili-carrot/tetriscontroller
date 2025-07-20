package com.example.tetriscontroller

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var tetrisView: TetrisView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tetrisView = findViewById(R.id.tetrisView)

        tetrisView.setOnTouchListener(object : View.OnTouchListener {
            private var startX = 0f
            private var startY = 0f

            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = event.x
                        startY = event.y
                    }
                    MotionEvent.ACTION_UP -> {
                        val dx = event.x - startX
                        val dy = event.y - startY

                        when {
                            dx > 100 -> tetrisView.game.moveRight()
                            dx < -100 -> tetrisView.game.moveLeft()
                            dy < -100 -> tetrisView.game.currentBlock.rotate(tetrisView.game)
                        }
                    }
                }
                return true
            }
        })
    }
}