package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentDownloadsongBinding

class DownloadSongFragment : Fragment(){
    lateinit var binding: FragmentDownloadsongBinding
    private var albumDatas = ArrayList<DownloadSong>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDownloadsongBinding.inflate(inflater, container, false)

        // 데이터 리스트 생성 더미 데이터
        // 실제로는 서버에서 갖고오는 것!
        albumDatas.apply {
            add(DownloadSong("daisy", "wave to earth", R.drawable.img_album_daisy))
            add(DownloadSong("멸종", "쏜애플(THORNAPPLE)", R.drawable.img_album_extinction))
            add(DownloadSong("맑고 묽게", "검정치마(The Black Skirts)", R.drawable.img_album_hawaii))
            add(DownloadSong("seasons", "wave to earth", R.drawable.img_album_seasons))
            add(DownloadSong("bad", "wave to earth", R.drawable.img_album_bad))
            add(DownloadSong("Find Me!", "더 폴스(The Poles)", R.drawable.img_album_findme))
            add(DownloadSong("등대", "하현상", R.drawable.img_album_ha))
            add(DownloadSong("끝의 시작", "나상현씨밴드", R.drawable.img_album_startofend))
            add(DownloadSong("시퍼런 봄", "쏜애플(THORNAPPLE)", R.drawable.img_album_spring))
            add(DownloadSong("하와이 검은 모래", "검정치마(The Black Skirts)", R.drawable.img_album_hawaii))
            add(DownloadSong("LOBSTER KING", "Tuesday Beach Club", R.drawable.img_album_lobsterking))
            add(DownloadSong("숲", "최유리", R.drawable.img_album_forest))
            add(DownloadSong("외딴섬 로맨틱", "잔나비", R.drawable.img_album_romantic))
        }

        // 어댑터와 데이터리스트 연결
        val downloadSongRVAdapter = DownloadSongRVAdapter(albumDatas)

        // 리사이클러뷰와 어댑터 연결
        binding.downloadSongRv.adapter = downloadSongRVAdapter

        // 레이아웃 매니저 설정
        binding.downloadSongRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        downloadSongRVAdapter.setMyItemClickListener(object: DownloadSongRVAdapter.MyItemClickListner{
            override fun onItemClick(album: DownloadSong) {
                TODO("Not yet implemented")
            }

            override fun onRemoveDownloadSong(position: Int) {
                downloadSongRVAdapter.removeItem(position)
            }
        })

        return binding.root
    }
}