package com.example.meutreino

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
val CHANNEL_2_ID = "channel2"
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContentView(R.layout.activity_main)
    }

    fun loadCadastro(view: View) {
        val intent = Intent(this, CadastrarActivity::class.java)
        startActivity(intent)
    }

    fun loadLista(view: View) {
        val intent = Intent(this, ListarActivity::class.java)
        startActivity(intent)
    }

    private fun createNotificationChannel() {
        val channel2 = NotificationChannel(
            CHANNEL_2_ID,
            "Channel 2",
            NotificationManager.IMPORTANCE_LOW
        )
        channel2.description = "This is Channel 2"
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channel2.id, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}