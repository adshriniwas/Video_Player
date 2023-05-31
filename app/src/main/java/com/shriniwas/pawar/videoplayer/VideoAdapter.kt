package com.shriniwas.pawar.videoplayer

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shriniwas.pawar.videoplayer.databinding.MoreFeaturesBinding
import com.shriniwas.pawar.videoplayer.databinding.RenameFieldBinding
import com.shriniwas.pawar.videoplayer.databinding.VideoMoreFeaturesBinding
import com.shriniwas.pawar.videoplayer.databinding.VideoViewBinding
import java.io.File


class VideoAdapter(private val context: Context, private var videoList: ArrayList<Video>, private val isFolder: Boolean = false) : RecyclerView.Adapter<VideoAdapter.MyHolder>(){
    class MyHolder(binding: VideoViewBinding): RecyclerView.ViewHolder(binding.root) {
        val title = binding.videoName
        val folder = binding.folderName
        val duration = binding.duration
        val image = binding.videoImg
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoAdapter.MyHolder {
        return MyHolder(VideoViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: VideoAdapter.MyHolder, position: Int) {
        holder.title.text = videoList[position].title
        holder.folder.text = videoList[position].folderName
        holder.duration.text = DateUtils.formatElapsedTime(videoList[position].duration/1000)
        Glide.with(context)
            .asBitmap()
            .load(videoList[position].artUri)
            .apply(RequestOptions().placeholder(R.mipmap.ic_video_player).centerCrop())
            .into(holder.image)
        holder.root.setOnClickListener {
            when{
                videoList[position].id == PlayerActivity.nowPlayingId -> {
                    sendIntent(pos = position, ref = "NowPlaying")
                }
                isFolder -> {
                    PlayerActivity.pipStatus = 1
                    sendIntent(pos = position, ref = "FolderActivity")
                }
                MainActivity.search -> {
                    PlayerActivity.pipStatus = 2
                    sendIntent(pos = position, ref = "SearchedVideos")
                }
                else -> {
                    PlayerActivity.pipStatus = 3
                    sendIntent(pos = position, ref = "AllVideos")
                }
            }
        }

        holder.root.setOnLongClickListener {

            val customDialog = LayoutInflater.from(context).inflate(R.layout.video_more_features, holder.root, false)
            val bindingMF = VideoMoreFeaturesBinding.bind(customDialog)
            val dialog = MaterialAlertDialogBuilder(context).setView(customDialog)
                .create()
            dialog.show()

            bindingMF.renameBtn.setOnClickListener {
                dialog.dismiss()
                val customDialogRF = LayoutInflater.from(context).inflate(R.layout.rename_field, holder.root, false)
                val bindingRF = RenameFieldBinding.bind(customDialogRF)
                val dialogRF = MaterialAlertDialogBuilder(context).setView(customDialogRF)
                    .setCancelable(false)
                    .setPositiveButton("Rename"){self, _ ->
                        val currentFile = File(videoList[position].path)
                        val newName = bindingRF.renameField.text
                        if (newName != null && currentFile.exists() && newName.toString().isNotEmpty()){
                            val newFile = File(currentFile.parentFile, newName.toString()+"."+currentFile.extension)
                            if (currentFile.renameTo(newFile)){
                                MediaScannerConnection.scanFile(context, arrayOf(newFile.toString()), arrayOf("video/*"), null)
                                MainActivity.videoList[position].title = newName.toString()
                                MainActivity.videoList[position].path = newFile.path
                                MainActivity.videoList[position].artUri = Uri.fromFile(newFile)
                                notifyItemChanged(position)
                            }
                            else{
                                Toast.makeText(context, "Permission Denied!!", Toast.LENGTH_SHORT).show()
                            }
                        }
                        self.dismiss()
                    }
                    .setNegativeButton("Cancel"){self, _ ->
                        self.dismiss()
                    }
                    .create()
                dialogRF.show()
                bindingRF.renameField.text = SpannableStringBuilder(videoList[position].title)
                dialogRF.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(
                    MaterialColors.getColor(context, R.attr.themeColor, Color.RED)
                )
                dialogRF.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(
                    MaterialColors.getColor(context, R.attr.themeColor, Color.RED)
                )
            }
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    private fun sendIntent(pos: Int, ref: String) {

        PlayerActivity.position = pos
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("class", ref)
        ContextCompat.startActivity(context, intent, null)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(searchList: ArrayList<Video>){
        videoList = ArrayList()
        videoList.addAll(searchList)
        notifyDataSetChanged()
    }
}