package com.shriniwas.pawar.videoplayer

import android.net.Uri
import java.time.Duration

data class Video(val id: String, val title: String, val duration: Long = 0, val folderName: String, val size: String
    , val path: String, val artUri: Uri)
