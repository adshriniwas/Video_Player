package com.shriniwas.pawar.videoplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.shriniwas.pawar.videoplayer.databinding.ActivityMainBinding
import com.shriniwas.pawar.videoplayer.databinding.ActivityPermissionBinding

class PermissionActivity : AppCompatActivity() {

    lateinit var binding: ActivityPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setTheme(R.style.coolPinkNav)
        setContentView(binding.root)

        binding.btnPermission.setOnClickListener(View.OnClickListener {
            if (checkPermission()){
                Toast.makeText(this@PermissionActivity,"permission already Granted.",Toast.LENGTH_SHORT).show()
                binding.tvPerm.text = "Permission Granted"
            }else {
                Toast.makeText(this@PermissionActivity,"permission was not Granted.",Toast.LENGTH_SHORT).show()
                requestRuntimePermission()
            }

        })

        binding.btnNext.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@PermissionActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        })


    }

    override fun onStart() {
        super.onStart()
        if (checkPermission()){
            val intent = Intent(this@PermissionActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{

        }
    }



    private fun requestRuntimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                try {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                    val uri = Uri.fromParts("package",this.packageName,null)

                    intent.data = uri
                    storageActivityResultLauncher.launch(intent)

                }catch (e : Exception){
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    storageActivityResultLauncher.launch(intent)
                }

        }else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE), 100)

        }
    }

    private val storageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()){
                binding.tvPerm.text = "Permission Granted"
                binding.btnNext.isEnabled = true
            }else {
                binding.tvPerm.text = "Permission Denied"
                binding.btnNext.isEnabled = false
            }
        }else {

        }
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        }else {
            val write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100){
            if (grantResults.isNotEmpty()){
                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED

                if (write && read){
                    binding.tvPerm.text = "Permission Granted"
                    binding.btnNext.isEnabled = true
                }else {
                    binding.tvPerm.text = "Permission Denied"
                    binding.btnNext.isEnabled = false
                }
            }
        }
    }
}