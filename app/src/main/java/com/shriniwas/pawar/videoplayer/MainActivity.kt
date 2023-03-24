package com.shriniwas.pawar.videoplayer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.shriniwas.pawar.videoplayer.databinding.ActivityMainBinding
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(R.style.coolPinkNav)
        setContentView(binding.root)
//        requestRuntimePermission()
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
                R.id.feedbackNav -> Toast.makeText(this, "feedback", Toast.LENGTH_SHORT).show()
                R.id.themesNav -> Toast.makeText(this, "themes", Toast.LENGTH_SHORT).show()
                R.id.sortOrderNav -> Toast.makeText(this, "sort order", Toast.LENGTH_SHORT).show()
                R.id.aboutNav -> Toast.makeText(this, "about", Toast.LENGTH_SHORT).show()
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

        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }




//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == 13){
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show()
//            }else {
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),13)
//            }
//        }
//    }
}