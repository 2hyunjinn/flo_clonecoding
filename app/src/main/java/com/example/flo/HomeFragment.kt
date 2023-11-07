package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

//        binding.homeTodayReleaseAlbumHv.post {
//            val albumImage = binding.homeTodayReleaseAlbumHv.findViewById<ImageView>(R.id.home_today_release_album_img_1_iv)
//            albumImage.setOnClickListener {
//                // AlbumFragment로 이동
//                val albumFragment = AlbumFragment()
//                parentFragmentManager.beginTransaction()
//                    .replace(R.id.main_frm, albumFragment)
//                    .addToBackStack(null)  // 뒤로 가기를 위해 스택에 추가
//                    .commit()
//            }
//        }

        // 데이터 리스트 생성 더미 데이터
        // 실제로는 서버에서 갖고오는 것!
        albumDatas.apply {
            add(Album("하와이 검은 모래", "검정치마", R.drawable.img_album_hawaii))
            add(Album("LOBSTER KING", "Tuesday Beach Club", R.drawable.img_album_lobsterking))
            add(Album("숲", "최유리", R.drawable.img_album_forest))
            add(Album("Find Me!", "더 폴스(The Poles)", R.drawable.img_album_findme))
            add(Album("등대", "하현상", R.drawable.img_album_ha))
        }

        albumDatas[0].songs.apply{ "하와이 검은 모래" }
        albumDatas[0].songs.apply{ "맑고 묽게" }


        // 어댑터와 데이터리스트 연결
        val albumRVAdapter = AlbumRVAdapter(albumDatas)

        // 리사이클러와 어댑터 연결
        binding.homeTodayReleaseAlbumRv.adapter = albumRVAdapter

        // 레이아웃 매니저 설정 (수평 - horizontal)
        binding.homeTodayReleaseAlbumRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        albumRVAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListner{
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }
        })

        val bannerAdapter = BannerVPAdatper(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val panelAdapter = PanelVPAdatper(this)
        panelAdapter.addFragment(PanelFragment(R.drawable.img_first_album_default, "데이지 PICK\n인디음악 모음"))
        panelAdapter.addFragment(PanelFragment(R.drawable.img_second_album, "노래방에서 부르고 싶은\n 노래 모음"))
        panelAdapter.addFragment(PanelFragment(R.drawable.img_third_album, "지친 하루 끝에 듣고 싶은\n 노래 모음"))
        binding.homePanelBackgroundVp.adapter = panelAdapter
        binding.homePanelBackgroundVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 패널용 ViewPager에 Indicator 설정
        val indicator = binding.homeIndicator
        indicator.setViewPager(binding.homePanelBackgroundVp)

        // 마지막 페이지에서 오른쪽으로 넘어가면 첫 번째 페이지로 이동하는 무한 루프 슬라이드
        val handler = Handler(Looper.getMainLooper())
        val panelViewPager = binding.homePanelBackgroundVp

        val sliderRunnable = object : Runnable {
            override fun run() {
                if (panelViewPager.currentItem < panelAdapter.itemCount - 1) {
                    // 현재 페이지가 마지막 페이지보다 이전이면 다음 페이지로 이동
                    panelViewPager.currentItem = panelViewPager.currentItem + 1
                } else {
                    // 마지막 페이지에서 오른쪽으로 넘어가면 첫 번째 페이지로 이동
                    panelViewPager.currentItem = 0
                }
                handler.postDelayed(this, 3000L) // 3초마다 슬라이드
            }
        }

        handler.post(sliderRunnable) // 슬라이더 실행

        return binding.root
    }

    // AlbumFragment로 이동
    private fun changeAlbumFragment(album: Album) {
        val albumFragment = AlbumFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frm, albumFragment.apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .addToBackStack(null)  // 뒤로 가기를 위해 스택에 추가
            .commit()
    }
}