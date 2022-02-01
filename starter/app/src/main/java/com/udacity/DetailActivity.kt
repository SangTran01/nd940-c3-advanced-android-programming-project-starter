package com.udacity

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import android.content.Intent




class DetailActivity : AppCompatActivity() {

    private var downloadId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        // get the 'extra' from the intent
        downloadId = this@DetailActivity.intent.getLongExtra("downloadId", 0)
        val downloadManager = this@DetailActivity.getSystemService(DownloadManager::class.java)
        getStatus(downloadManager, downloadId)


        val button: Button = findViewById(R.id.button_ok)
        button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    fun getStatus(mgr: DownloadManager, downloadId: Long) {
        val c: Cursor = mgr.query(DownloadManager.Query().setFilterById(downloadId))
        c.moveToFirst()
        setStatusMessage(c)
    }

    private fun setStatusMessage(c: Cursor) {
        val nameTextView: TextView = findViewById(R.id.textview_filename)
        nameTextView.text = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE))

        val statusTextView : TextView = findViewById(R.id.textview_status)
        statusTextView.text =
            when (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                DownloadManager.STATUS_FAILED -> "Download failed!"
                DownloadManager.STATUS_PAUSED -> "Download paused!"
                DownloadManager.STATUS_PENDING -> "Download pending!"
                DownloadManager.STATUS_RUNNING -> "Download in progress!"
                DownloadManager.STATUS_SUCCESSFUL -> "Download complete!"
                else -> "Download is nowhere in sight"
            }

        statusTextView.setTextColor(
            when (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                DownloadManager.STATUS_FAILED -> getColor(R.color.fail)
                DownloadManager.STATUS_PAUSED -> getColor(R.color.colorPrimaryDark)
                DownloadManager.STATUS_PENDING -> getColor(R.color.colorPrimaryDark)
                DownloadManager.STATUS_RUNNING -> getColor(R.color.colorPrimaryDark)
                DownloadManager.STATUS_SUCCESSFUL -> getColor(R.color.colorPrimaryDark)
                else -> getColor(R.color.fail)
            }
        )
    }
}
