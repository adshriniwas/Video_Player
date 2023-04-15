package com.shriniwas.pawar.videoplayer

import android.media.browse.MediaBrowser
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem


import com.shriniwas.pawar.videoplayer.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    companion object {
        private lateinit var player: ExoPlayer
        lateinit var playerList: ArrayList<Video>
        var position: Int = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
        }
        binding = ActivityPlayerBinding.inflate(layoutInflater)


        setTheme(R.style.playerActivityTheme)
        setContentView(binding.root)
        //        for immersive mode
        WindowCompat.setDecorFitsSystemWindows(window,true)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.statusBars())
//            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE

        }

        initializeLayout()
        initializeBinding()

    }

    private fun initializeLayout() {
        when(intent.getStringExtra("class")){
            "AllVideos" -> {
                playerList = ArrayList()
                playerList.addAll(MainActivity.videoList)
                createPlayer()
            }
            "FolderActivity" -> {
                playerList = ArrayList()
                playerList.addAll(FoldersActivity.currentFolderVideos)
                createPlayer()
            }

        }

    }

    private fun initializeBinding() {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.playPauseBtn.setOnClickListener {
            if (player.isPlaying) pauseVideo()
            else playVideo()
        }

        binding.nextBtn.setOnClickListener {
            nextPrevVideo()
        }

        binding.prevBtn.setOnClickListener {
            nextPrevVideo(isNext = false)
        }
    }

    private fun createPlayer() {

        binding.videoTitle.text = playerList[position].title
        binding.videoTitle.isSelected = true

        player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player


        val mediaItem = MediaItem.fromUri(playerList[position].artUri)

        player.setMediaItem(mediaItem)
        player.prepare()
        playVideo()
    }

    private fun playVideo(){
        binding.playPauseBtn.setImageResource(R.drawable.pause_icon)
        player.play()
    }

    private fun pauseVideo(){
        binding.playPauseBtn.setImageResource(R.drawable.play_icon)
        player.pause()
    }

    private fun nextPrevVideo(isNext: Boolean = true) {
        if (isNext) {
            player.stop()
            setPosition()
            createPlayer()
        }
        else {
            player.stop()
            setPosition(isIncrement = false)
            createPlayer()
        }

    }

    private fun setPosition(isIncrement: Boolean = true) {
        if (isIncrement){
            if (playerList.size -1 == position){
                position = 0
            }else{
                ++position
            }
        }else {
            if (position == 0){
                position = playerList.size - 1
            }else{
                --position
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}