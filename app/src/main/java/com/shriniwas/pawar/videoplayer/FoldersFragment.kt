package com.shriniwas.pawar.videoplayer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.shriniwas.pawar.videoplayer.databinding.FragmentFoldersBinding
import com.shriniwas.pawar.videoplayer.databinding.FragmentVideosBinding


class FoldersFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        requireContext().theme.applyStyle(MainActivity.themesList[MainActivity.themeIndex], true)
        val view = inflater.inflate(R.layout.fragment_folders, container, false)
        val binding = FragmentFoldersBinding.bind(view)


        binding.foldersRV.setHasFixedSize(true)
        binding.foldersRV.setItemViewCacheSize(10)

        binding.foldersRV.layoutManager = LinearLayoutManager(requireContext())
        binding.foldersRV.adapter = FoldersAdapter(requireContext(), MainActivity.folderList)
        binding.totalFolders.text = "Total Folders: ${MainActivity.folderList.size}"
        return view
    }



}