package com.shriniwas.pawar.videoplayer

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri

import android.text.SpannableStringBuilder
import android.text.format.DateUtils
import android.text.format.Formatter
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat

import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shriniwas.pawar.videoplayer.databinding.DetailsViewBinding
import com.shriniwas.pawar.videoplayer.databinding.VideoMoreFeaturesBinding
import com.shriniwas.pawar.videoplayer.databinding.VideoViewBinding
import java.io.File


class VideoAdapter(private val context: Context, private var videoList: ArrayList<Video>, private val isFolder: Boolean = false) : RecyclerView.Adapter<VideoAdapter.MyHolder>(){
    var mInterstitialAd: InterstitialAd? = null


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

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: VideoAdapter.MyHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.title.text = videoList[position].title
        holder.folder.text = videoList[position].folderName
        holder.duration.text = DateUtils.formatElapsedTime(videoList[position].duration/1000)
        Glide.with(context)
            .asBitmap()
            .load(videoList[position].artUri)
            .apply(RequestOptions().placeholder(R.mipmap.ic_video_player).centerCrop())
            .into(holder.image)

        holder.root.setOnClickListener {

            if(videoList[position].id == PlayerActivity.nowPlayingId) {
                val dialogDF = MaterialAlertDialogBuilder(context)
                    .setTitle("Do you want to play Video?")
                    .setPositiveButton("Resume") { self, _ ->
                        var adRequest = AdRequest.Builder().build()


                        InterstitialAd.load(
                            context,
                            context.getString(R.string.interstitial_ad),
                            adRequest,
                            object : InterstitialAdLoadCallback() {
                                override fun onAdFailedToLoad(adError: LoadAdError) {
                                    mInterstitialAd = null
                                }

                                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                    mInterstitialAd = interstitialAd
                                }
                            })

                        mInterstitialAd?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdClicked() {
                                    // Called when a click is recorded for an ad.
                                    Log.d("soccer", "Ad was clicked.")
                                }

                                override fun onAdDismissedFullScreenContent() {
                                    // Called when ad is dismissed.
                                    Log.d("soccer", "Ad dismissed fullscreen content.")
                                    mInterstitialAd = null
                                    when {
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

                                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                    // Called when ad fails to show.
                                    Log.e("soccer", "Ad failed to show fullscreen content.")
                                    mInterstitialAd = null
                                }

                                override fun onAdImpression() {
                                    // Called when an impression is recorded for an ad.
                                    Log.d("soccer", "Ad recorded an impression.")
                                }

                                override fun onAdShowedFullScreenContent() {
                                    // Called when ad is shown.
                                    Log.d("soccer", "Ad showed fullscreen content.")
                                }
                            }

                        if (mInterstitialAd != null) {
                            mInterstitialAd?.show(context as Activity)
                        } else {
                            Log.d("TAG", "The interstitial ad wasn't ready yet.")
                            when {
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
                    }
                    .setNegativeButton("Play From Begining") { self, _ ->
                        var adRequest = AdRequest.Builder().build()

                        InterstitialAd.load(
                            context,
                            context.getString(R.string.interstitial_ad),
                            adRequest,
                            object : InterstitialAdLoadCallback() {
                                override fun onAdFailedToLoad(adError: LoadAdError) {

                                    mInterstitialAd = null
                                }

                                override fun onAdLoaded(interstitialAd: InterstitialAd) {

                                    mInterstitialAd = interstitialAd
                                }
                            })

                        mInterstitialAd?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdClicked() {
                                    // Called when a click is recorded for an ad.
                                    Log.d("soccer", "Ad was clicked.")
                                }

                                override fun onAdDismissedFullScreenContent() {
                                    // Called when ad is dismissed.
                                    Log.d("soccer", "Ad dismissed fullscreen content.")
                                    mInterstitialAd = null
                                    when {
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

                                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                    // Called when ad fails to show.
                                    Log.e("soccer", "Ad failed to show fullscreen content.")
                                    mInterstitialAd = null


                                }

                                override fun onAdImpression() {
                                    // Called when an impression is recorded for an ad.
                                    Log.d("soccer", "Ad recorded an impression.")
                                }

                                override fun onAdShowedFullScreenContent() {
                                    // Called when ad is shown.
                                    Log.d("soccer", "Ad showed fullscreen content.")
                                }
                            }


                        if (mInterstitialAd != null) {
                            mInterstitialAd?.show(context as Activity)
                        } else {
                            Log.d("TAG", "The interstitial ad wasn't ready yet.")
                            when {
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
                    }.create()
                dialogDF.show()
                dialogDF.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(
                    MaterialColors.getColor(context, R.attr.themeColor, Color.RED)
                )
                dialogDF.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(
                    MaterialColors.getColor(context, R.attr.themeColor, Color.RED)
                )
            }else {

//                It doesnt show dialogue for unresumed video
                var adRequest = AdRequest.Builder().build()

                InterstitialAd.load(
                    context,
                    context.getString(R.string.interstitial_ad),
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {

                            mInterstitialAd = null
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {

                            mInterstitialAd = interstitialAd
                        }
                    })

                mInterstitialAd?.fullScreenContentCallback =
                    object : FullScreenContentCallback() {
                        override fun onAdClicked() {
                            // Called when a click is recorded for an ad.
                            Log.d("soccer", "Ad was clicked.")
                        }

                        override fun onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed.
                            Log.d("soccer", "Ad dismissed fullscreen content.")
                            mInterstitialAd = null
                            when {
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

                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                            // Called when ad fails to show.
                            Log.e("soccer", "Ad failed to show fullscreen content.")
                            mInterstitialAd = null


                        }

                        override fun onAdImpression() {
                            // Called when an impression is recorded for an ad.
                            Log.d("soccer", "Ad recorded an impression.")
                        }

                        override fun onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                            Log.d("soccer", "Ad showed fullscreen content.")
                        }
                    }


                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(context as Activity)
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
                    when {
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
            }





        }

        holder.root.setOnLongClickListener {

            val customDialog = LayoutInflater.from(context).inflate(R.layout.video_more_features, holder.root, false)
            val bindingMF = VideoMoreFeaturesBinding.bind(customDialog)
            val dialog = MaterialAlertDialogBuilder(context).setView(customDialog)
                .create()
            dialog.show()

//            bindingMF.renameBtn.setOnClickListener {
//                dialog.dismiss()
//                val customDialogRF = LayoutInflater.from(context).inflate(R.layout.rename_field, holder.root, false)
//                val bindingRF = RenameFieldBinding.bind(customDialogRF)
//                val dialogRF = MaterialAlertDialogBuilder(context).setView(customDialogRF)
//                    .setCancelable(false)
//                    .setPositiveButton("Rename"){self, _ ->
//                        val currentFile = File(videoList[position].path)
//                        val newName = bindingRF.renameField.text
//                        if (newName != null && currentFile.exists() && newName.toString().isNotEmpty()){
//                            val newFile = File(currentFile.parentFile, newName.toString()+"."+currentFile.extension)
//                                currentFile.renameTo(newFile)
//                                MediaScannerConnection.scanFile(context, arrayOf(newFile.toString()), arrayOf("video/*"), null)
//                                when{
//                                    MainActivity.search -> {
//                                        MainActivity.searchList[position].title = newName.toString()
//                                        MainActivity.searchList[position].path = newFile.path
//                                        MainActivity.searchList[position].artUri = Uri.fromFile(newFile)
//                                        notifyItemChanged(position)
//                                    }
//                                    isFolder -> {
//                                        FoldersActivity.currentFolderVideos[position].title = newName.toString()
//                                        FoldersActivity.currentFolderVideos[position].path = newFile.path
//                                        FoldersActivity.currentFolderVideos[position].artUri = Uri.fromFile(newFile)
//                                        notifyItemChanged(position)
//                                        MainActivity.dataChanged = true
//                                    }
//                                    else -> {
//                                        MainActivity.videoList[position].title = newName.toString()
//                                        MainActivity.videoList[position].path = newFile.path
//                                        MainActivity.videoList[position].artUri = Uri.fromFile(newFile)
//                                        notifyItemChanged(position)
//                                    }
//                                }
//
//                        }
//                        self.dismiss()
//                    }
//                    .setNegativeButton("Cancel"){self, _ ->
//                        self.dismiss()
//                    }
//                    .create()
//                dialogRF.show()
//                bindingRF.renameField.text = SpannableStringBuilder(videoList[position].title)
//                dialogRF.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(
//                    MaterialColors.getColor(context, R.attr.themeColor, Color.RED)
//                )
//                dialogRF.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(
//                    MaterialColors.getColor(context, R.attr.themeColor, Color.RED)
//                )
//            }

            bindingMF.shareBtn.setOnClickListener {
                dialog.dismiss()
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "video/*"
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(videoList[position].path))
                ContextCompat.startActivity(context, Intent.createChooser(shareIntent, "Sharing Video File!!!"), null)
            }

            bindingMF.infoBtn.setOnClickListener {
                dialog.dismiss()
                val customDialogIF = LayoutInflater.from(context).inflate(R.layout.details_view, holder.root, false)
                val bindingIF = DetailsViewBinding.bind(customDialogIF)
                val dialogIF = MaterialAlertDialogBuilder(context).setView(customDialogIF)
                    .setCancelable(false)
                    .setPositiveButton("Ok"){self, _ ->

                        self.dismiss()
                    }
                    .create()
                dialogIF.show()
                val infoText = SpannableStringBuilder().bold { append("DETAILS\n\nName: ") }.append(videoList[position].title)
                    .bold { append("\n\nDuration: ") }.append(DateUtils.formatElapsedTime(videoList[position].duration/1000))
                    .bold { append("\n\nFile Size: ") }.append(Formatter.formatShortFileSize(context, videoList[position].size.toLong()))
                    .bold { append("\n\nLocation: ") }.append(videoList[position].path)

                bindingIF.detailTV.text = infoText
                dialogIF.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(
                    MaterialColors.getColor(context, R.attr.themeColor, Color.RED)
                )

            }

            bindingMF.deleteBtn.setOnClickListener {
                dialog.dismiss()
                val dialogDF = MaterialAlertDialogBuilder(context)
                    .setTitle("Delete Video?")
                    .setMessage(videoList[position].title)
                    .setPositiveButton("Yes"){self, _ ->
                        val file = File(videoList[position].path)
                        if (file.exists() && file.delete()){
                            MediaScannerConnection.scanFile(context, arrayOf(file.path),
                                arrayOf("video/*")
                                , null)
                            when{
                                MainActivity.search ->{
                                    MainActivity.dataChanged = true
                                    videoList.removeAt(position)
                                    notifyDataSetChanged()
                                }
                                isFolder -> {
                                    MainActivity.dataChanged = true
                                    FoldersActivity.currentFolderVideos.removeAt(position)
                                    notifyDataSetChanged()
                                }
                                else -> {
                                    MainActivity.videoList.removeAt(position)
                                    notifyDataSetChanged()
                                }
                            }
                        }
                        else{
                            Toast.makeText(context, "permission denied", Toast.LENGTH_SHORT).show()
                        }
                        self.dismiss()
                    }
                    .setNegativeButton("No"){self, _ ->
                        self.dismiss()
                    }
                    .create()
                dialogDF.show()
                dialogDF.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(
                    MaterialColors.getColor(context, R.attr.themeColor, Color.RED)
                )
                dialogDF.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(
                    MaterialColors.getColor(context, R.attr.themeColor, Color.RED)
                )
            }
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

     fun loadAndManageAds() {


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