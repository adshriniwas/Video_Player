package com.shriniwas.pawar.videoplayer

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.shriniwas.pawar.videoplayer.databinding.ActivityFoldersBinding
import java.io.File

class FoldersActivity : AppCompatActivity() {

    private lateinit var checkNetworkConnection: CheckNetworkConnection

    companion object {
        lateinit var currentFolderVideos: ArrayList<Video>
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFoldersBinding.inflate(layoutInflater)
        setTheme(MainActivity.themesList[MainActivity.themeIndex])
        setContentView(binding.root)

        callNetworkConnection()

        val position = intent.getIntExtra("position", 0)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = MainActivity.folderList[position].folderName

        currentFolderVideos = getAllVideos(MainActivity.folderList[position].id)

        binding.videoRVFA.setHasFixedSize(true)
        binding.videoRVFA.setItemViewCacheSize(10)

        binding.videoRVFA.layoutManager = LinearLayoutManager(this@FoldersActivity)
        binding.videoRVFA.adapter = VideoAdapter(this@FoldersActivity, currentFolderVideos, isFolder = true, )
        binding.totalVideosFA.text = "Total Videos: ${currentFolderVideos.size}"

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            finish()
        return true
    }

    private fun getAllVideos(folderId: String): ArrayList<Video>{
        val tempList = ArrayList<Video>()
        val selection = MediaStore.Video.Media.BUCKET_ID + " like? "

        val projection = arrayOf(
            MediaStore.Video.Media.TITLE, MediaStore.Video.Media.SIZE, MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DURATION, MediaStore.Video.Media.BUCKET_ID)
        val cursor = this.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, selection, arrayOf(folderId),
            MediaStore.Video.Media.DATE_ADDED + " DESC")

        if (cursor != null){
            if (cursor.moveToNext()) {
                do {
                    val titleC =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE))
                    val idC =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                    val folderC =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                    val sizeC =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                    val pathC =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                    val durationC =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
                            .toLong()

                    try {
                        val file = File(pathC)
                        val artUriC = Uri.fromFile(file)
                        val video = Video(
                            title = titleC,
                            id = idC,
                            folderName = folderC,
                            duration = durationC,
                            size = sizeC,
                            path = pathC,
                            artUri = artUriC
                        )
                        if (file.exists()) tempList.add(video)



                    } catch (e: Exception) {

                    }

                } while (cursor.moveToNext())
                cursor?.close()
            }
        }

        return tempList
    }

    fun loadBannerAds() {
        MobileAds.initialize(this@FoldersActivity)
        val adRequest: AdRequest = AdRequest.Builder().build()
        this@FoldersActivity.findViewById<AdView>(R.id.adView2).loadAd(adRequest)
    }

    private fun callNetworkConnection() {
        checkNetworkConnection = CheckNetworkConnection(application)
        checkNetworkConnection.observe(this) { isConnected ->
            if (isConnected) {
                this@FoldersActivity.findViewById<AdView>(R.id.adView2).visibility = View.VISIBLE
                loadBannerAds()
            } else {
                this@FoldersActivity.findViewById<AdView>(R.id.adView2).visibility = View.GONE
                Toast.makeText(this, "Network Lost", Toast.LENGTH_SHORT).show()
            }
        }

    }
}