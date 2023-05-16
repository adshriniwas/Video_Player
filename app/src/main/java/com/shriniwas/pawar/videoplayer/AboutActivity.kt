package com.shriniwas.pawar.videoplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shriniwas.pawar.videoplayer.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(MainActivity.themesList[MainActivity.themeIndex])
        val binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "About"
        binding.aboutTxt.text = "Developed By: Shriniwas Pawar \n"+
                "\n"+
                "I Hope you Loved This Video Player App... \n"+
                "Keep Supporting Me."


    }
}