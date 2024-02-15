package com.example.igdownloader.ImageFetcher

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File

suspend fun download(link: String, context: Context) {
    val picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val directory = File(picturesDirectory, "igdownloader")
    if (!directory.exists()) {
        directory.mkdirs()
    }
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val uri = Uri.parse(link)
    var fileName = ""
    if("jpg" in link) {
        fileName = "image_${System.currentTimeMillis()}.jpg"
    }
    else {
        fileName = "video_${System.currentTimeMillis()}.mp4"
    }
    val request = DownloadManager.Request(uri).apply {
        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        setTitle("Downloading")
        setDescription("Downloading $fileName")
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "igdownloader/$fileName")
    }

    downloadManager.enqueue(request)
}