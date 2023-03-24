package com.shriniwas.pawar.videoplayer

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

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_folders, container, false)
        val tempList = ArrayList<String>()

        tempList.add("Folder 1")
        tempList.add("Folder 2")
        tempList.add("Folder 3")
        tempList.add("Folder 4")
        tempList.add("Folder 5")
        tempList.add("Folder 6")
        tempList.add("Folder 7")
        tempList.add("Folder 8")
        tempList.add("Folder 9")
        tempList.add("Folder 10")
        tempList.add("Folder 11")
        tempList.add("Folder 12")


        val binding = FragmentFoldersBinding.bind(view)


        binding.foldersRV.setHasFixedSize(true)
        binding.foldersRV.setItemViewCacheSize(10)

        binding.foldersRV.layoutManager = LinearLayoutManager(requireContext())
        binding.foldersRV.adapter = FoldersAdapter(requireContext(), tempList)
        return view
    }



}