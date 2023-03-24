package com.shriniwas.pawar.videoplayer

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shriniwas.pawar.videoplayer.databinding.FoldersViewBinding
import com.shriniwas.pawar.videoplayer.databinding.VideoViewBinding

class FoldersAdapter(private val context: Context, private var foldersList: ArrayList<String>) : RecyclerView.Adapter<FoldersAdapter.MyHolder>(){
    class MyHolder(binding: FoldersViewBinding): RecyclerView.ViewHolder(binding.root) {
        val folderName = binding.folderNameFV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoldersAdapter.MyHolder {
        return MyHolder(FoldersViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: FoldersAdapter.MyHolder, position: Int) {
        holder.folderName.text = foldersList[position]
    }

    override fun getItemCount(): Int {
        return foldersList.size
    }
}