package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.example.flo.databinding.FragmentSongBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {
    lateinit var  binding : FragmentAlbumBinding
    private var gson: Gson = Gson()

    private val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        // arguments에서 data를 꺼내서 Json형태로 앨범객체를 변환해서 albumJson으로 저장
        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson, Album::class.java)
        setInit(album)

        binding.albumBtnBackIv.setOnClickListener{
            val homeFragment = HomeFragment()  // AlbumFragment 객체 생성
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, homeFragment)  // homeFragment 객체를 넘겨줌
                .commitAllowingStateLoss()
        }

        val albumAdater = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumAdater

        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
            tab, position ->
            tab.text = information[position]
        }.attach()


        return binding.root
    }

    private fun setInit(album: Album){
        binding.albumTitleTv.text = album.title.toString()
        binding.albumSingerTv.text = album.singer.toString()
        binding.albumImgIv.setImageResource(album.coverImg!!)
    }
}