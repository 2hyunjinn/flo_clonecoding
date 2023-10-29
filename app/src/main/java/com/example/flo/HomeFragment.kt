package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeTodayReleaseAlbumHv.post {
            val albumImage = binding.homeTodayReleaseAlbumHv.findViewById<ImageView>(R.id.home_today_release_album_img_1_iv)
            albumImage.setOnClickListener {
                // AlbumFragment로 이동
                val albumFragment = AlbumFragment()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, albumFragment)
                    .addToBackStack(null)  // 뒤로 가기를 위해 스택에 추가
                    .commit()
            }
        }

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
}