package com.ucb.helpet.features.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ucb.helpet.MainActivity
import com.ucb.helpet.R
import kotlin.random.Random

class HelpetMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Log para verificar si el mensaje se recibe
        Log.d("FCM_MESSAGE", "FROM: ${remoteMessage.from}")
        Log.d("FCM_MESSAGE", "DATA: ${remoteMessage.data}")

        super.onMessageReceived(remoteMessage)

        val data = remoteMessage.data
        val notification = remoteMessage.notification

        val title = notification?.title ?: "Helpet"
        val body = notification?.body ?: "Tienes una nueva notificación"

        // Extract Custom Data
        val petId = data["petId"]
        val type = data["type"]

        showNotification(title, body, petId, type)
    }

    private fun showNotification(title: String, body: String, petId: String?, type: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (petId != null) putExtra("petId", petId)
            if (type != null) putExtra("notificationType", type)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "helpet_urgent"

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Alertas de Mascotas",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(Random.nextInt(), notificationBuilder.build())
    }
}
