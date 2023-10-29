package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentDetailBinding
import com.example.flo.databinding.FragmentSongBinding

class SongFragment : Fragment() {
    lateinit var binding: FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        // Toast 메세지
        binding.albumTrack1Lo.setOnClickListener{
            Log.d("MyApp", "Click detected on songAlbumInfo1")
            Toast.makeText(activity, "틀린질문", Toast.LENGTH_LONG).show()
        }

        binding.albumTrack2Lo.setOnClickListener{
            Toast.makeText(activity, "Lester Burnham", Toast.LENGTH_SHORT).show()
        }

        binding.albumTrack3Lo.setOnClickListener{
            Toast.makeText(activity, "섬(Queen of Diamonds)", Toast.LENGTH_SHORT).show()
        }

        binding.albumTrack4Lo.setOnClickListener{
            Toast.makeText(activity, "상수역", Toast.LENGTH_SHORT).show()
        }

        // mix 버튼
        binding.albumBtnToggleOffIv.setOnClickListener {
            Log.d("MyApp", "Click detected on albumBtnToggleOff")
            setMixBtnStatus(false)
        }

        binding.albumBtnToggleOnIv.setOnClickListener {
            Log.d("MyApp", "Click detected on albumBtnToggleOn")
            setMixBtnStatus(true)
        }

        return binding.root
    }

    fun setMixBtnStatus(isOff : Boolean){
        if(isOff){
            binding.albumBtnToggleOffIv.visibility = View.VISIBLE
            binding.albumBtnToggleOnIv.visibility = View.GONE
        }
        else{
            binding.albumBtnToggleOffIv.visibility = View.GONE
            binding.albumBtnToggleOnIv.visibility = View.VISIBLE
        }
    }
}