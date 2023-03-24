package com.shriniwas.pawar.videoplayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.shriniwas.pawar.videoplayer.databinding.FragmentVideosBinding


class VideosFragment : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_videos, container, false)

        val binding = FragmentVideosBinding.bind(view)
        val tempList = ArrayList<String>()
        tempList.add("First Video")
        tempList.add("Second Video")
        tempList.add("Third Video")
        tempList.add("Fourth Video")
        tempList.add("Fifth Video")
        tempList.add("Sixth Video")

        binding.videoRV.setHasFixedSize(true)
        binding.videoRV.setItemViewCacheSize(10)

        binding.videoRV.layoutManager = LinearLayoutManager(requireContext())
        binding.videoRV.adapter = VideoAdapter(requireContext(), tempList)
        return view
    }


}