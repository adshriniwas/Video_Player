package com.shriniwas.pawar.videoplayer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shriniwas.pawar.videoplayer.databinding.ActivityMainBinding
import com.shriniwas.pawar.videoplayer.databinding.MoreFeaturesBinding
import com.shriniwas.pawar.videoplayer.databinding.ThemeViewBinding
import java.io.File
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    companion object{
        lateinit var videoList: ArrayList<Video>
        lateinit var folderList: ArrayList<Folder>
        lateinit var searchList: ArrayList<Video>
        var search: Boolean = false
        var themeIndex: Int = 0
        val themesList = arrayOf(R.style.coolPinkNav, R.style.coolBlueNav, R.style.coolPurpleNav, R.style.coolGreenNav, R.style.coolRedNav, R.style.coolBlackNav)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val editor = getSharedPreferences("Themes", MODE_PRIVATE)
        themeIndex = editor.getInt("themeIndex", 0)

        setTheme(themesList[themeIndex])
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
//        requestRuntimePermission()
        folderList = ArrayList()
        videoList = getAllVideos()
        toggle = ActionBarDrawerToggle(this, binding.root, R.string.open, R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setFragment(VideosFragment())

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.videoView -> setFragment(VideosFragment())
                R.id.foldersView -> setFragment(FoldersFragment())
            }
            return@setOnItemSelectedListener true
        }

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
//                R.id.feedbackNav -> Toast.makeText(this, "feedback", Toast.LENGTH_SHORT).show()
                R.id.themesNav -> {
                    val customDialog = LayoutInflater.from(this).inflate(R.layout.theme_view, binding.root, false)
                    val bindingTV = ThemeViewBinding.bind(customDialog)
                    val dialog = MaterialAlertDialogBuilder(this).setView(customDialog)
                        .setTitle("Select Theme")
                        .create()
                    dialog.show()
                    when(themeIndex){
                        0 -> bindingTV.themePink.setBackgroundColor(Color.YELLOW)
                        1 -> bindingTV.themeBlue.setBackgroundColor(Color.YELLOW)
                        2 -> bindingTV.themePurple.setBackgroundColor(Color.YELLOW)
                        3 -> bindingTV.themeGreen.setBackgroundColor(Color.YELLOW)
                        4 -> bindingTV.themeRed.setBackgroundColor(Color.YELLOW)
                        5 -> bindingTV.themeBlack.setBackgroundColor(Color.YELLOW)
                    }
                    bindingTV.themePink.setOnClickListener { saveTheme(0) }
                    bindingTV.themeBlue.setOnClickListener { saveTheme(1) }
                    bindingTV.themePurple.setOnClickListener { saveTheme(2) }
                    bindingTV.themeGreen.setOnClickListener { saveTheme(3) }
                    bindingTV.themeRed.setOnClickListener { saveTheme(4) }
                    bindingTV.themeBlack.setOnClickListener { saveTheme(5) }
                }
                R.id.sortOrderNav -> Toast.makeText(this, "sort order", Toast.LENGTH_SHORT).show()
                R.id.aboutNav -> startActivity(Intent(this, AboutActivity::class.java))
                R.id.exitNav -> exitProcess(1)
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun setFragment(fragment : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentFL, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val gradientList = arrayOf(R.drawable.pink_gradient, R.drawable.blue_gradient, R.drawable.purple_gradient,
            R.drawable.green_gradient, R.drawable.red_gradient, R.drawable.black_gradient)

        findViewById<LinearLayout>(R.id.gradientLayout).setBackgroundResource(gradientList[themeIndex])

        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getAllVideos(): ArrayList<Video>{
        val tempList = ArrayList<Video>()
        val tempFoldersList = ArrayList<String>()
        val projection = arrayOf(MediaStore.Video.Media.TITLE, MediaStore.Video.Media.SIZE, MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DURATION, MediaStore.Video.Media.BUCKET_ID)
        val cursor = this.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null,
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
                    val folderIdC =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID))
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

                        //for adding folders
                        if (!tempFoldersList.contains(folderC)){
                            tempFoldersList.add(folderC)
                            folderList.add(Folder(id = folderIdC, folderName = folderC))
                        }

                    } catch (e: Exception) {

                    }

                } while (cursor.moveToNext())
                cursor?.close()
            }
        }

        return tempList
    }

    private fun saveTheme(index: Int){
        val editor = getSharedPreferences("Themes", MODE_PRIVATE).edit()
        editor.putInt("themeIndex", index)
        editor.apply()

//        for restarting app
        finish()
        startActivity(intent)
    }

}