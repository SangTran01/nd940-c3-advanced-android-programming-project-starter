package com.udacity.util

import android.app.*
import android.content.Context

import android.content.Intent
import android.database.Cursor
import android.os.SystemClock
import android.util.Log
import android.view.View

import androidx.core.content.ContextCompat.startActivity

import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.DetailActivity
import com.udacity.MainActivity
import com.udacity.R

const val CHANNEL_ID = "channelId"
const val NOTIFICATION_ID = 1001

fun sendNotification(context: Context, intentValue: Long, projectTitle: String) {

    val intent = Intent(context, DetailActivity::class.java)
    intent.putExtra("downloadId", intentValue)
    val pendingIntent =
        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


    val notification = NotificationCompat.Builder(context, CHANNEL_ID).apply {
        setPriority(NotificationCompat.PRIORITY_HIGH)
        setSmallIcon(R.drawable.ic_assistant_black_24dp)
        setContentTitle("The Project $projectTitle is downloaded")
        setContentText("check the status")
        setContentIntent(pendingIntent)
        setAutoCancel(true)
    }.build()

    val manager = context.getSystemService(NotificationManager::class.java)
    manager.notify(NOTIFICATION_ID, notification)
}