package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.R.id
import android.app.NotificationChannel
import android.database.Cursor
import android.os.Build
import android.widget.RadioButton
import com.udacity.util.CHANNEL_ID
import com.udacity.util.sendNotification


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(CHANNEL_ID, "Download Git Repo Channel")

        custom_button.setOnClickListener {
            when (radio_group.checkedRadioButtonId) {
                -1 -> Toast.makeText(this, "Please Select something", Toast.LENGTH_SHORT).show()
                else -> {
                    val button: RadioButton =
                        radio_group.findViewById(radio_group.checkedRadioButtonId)
                    setUrl(button.id)
                    download()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) ?: 0L
            sendNotification(this@MainActivity, id, SelectedProject!!)
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(ProjectTitle)
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.

    }

    private fun createChannel(Id: String, name: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Id, name, NotificationManager.IMPORTANCE_HIGH)
            channel.setShowBadge(true)
            channel.description = "Channel Description"

            val manager = this@MainActivity.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }


    private fun setUrl(id: Int) {
        URL = when (id) {
            R.id.glide -> "https://github.com/bumptech/glide/archive/master.zip"
            R.id.loadapp -> "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
            else -> "https://github.com/square/retrofit/archive/master.zip"
        }

        SelectedProject = when (id) {
            R.id.glide -> "1"
            R.id.loadapp -> "2"
            else -> "3"
        }

        ProjectTitle = when (id) {
            R.id.glide -> getString(R.string.glide_name)
            R.id.loadapp -> getString(R.string.loadapp_name)
            else -> getString(R.string.retro_name)
        }
    }

    companion object {
        private var SelectedProject : String? = null
        private var ProjectTitle : String? = null
        private var URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
    }

}
