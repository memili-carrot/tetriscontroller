package com.example.tetriscontroller.wear.presentation

import com.example.tetriscontroller.wear.R
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {

    private lateinit var messageClient: MessageClient
    private val messagePath = "/tetris_input"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageClient = Wearable.getMessageClient(this)

        findViewById<Button>(R.id.buttonLeft).setOnClickListener {
            sendCommand("MOVE_LEFT")
        }
        findViewById<Button>(R.id.buttonRight).setOnClickListener {
            sendCommand("MOVE_RIGHT")
        }
        findViewById<Button>(R.id.buttonRotate).setOnClickListener {
            sendCommand("ROTATE")
        }
        findViewById<Button>(R.id.buttonDrop).setOnClickListener {
            sendCommand("DROP")
        }
    }

    private fun sendCommand(command: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val nodes = Wearable.getNodeClient(applicationContext).connectedNodes.await()
            for (node in nodes) {
                messageClient.sendMessage(node.id, messagePath, command.toByteArray())
            }
        }
    }
}