package com.example.flo

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.util.Log.*
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson
import java.util.Timer

class SongActivity : AppCompatActivity() {
    // 전역변수 선언
    lateinit var binding: ActivitySongBinding
    lateinit var song: Song
    lateinit var timer: Timer

    // media player 객체 생성
    private var mediaPlayer: MediaPlayer? = null

    // Gson 라이브러리 객체 생성
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater) // 레이아웃을 메모리에 객체화
        setContentView(binding.root) // activity_song.xml을 화면에 설정

        // 노래 초기화 함수 호출
        initSong()
        // 플레이어 설정 함수 호출
        setPlayer(song)

        // 뒤로 가기 버튼 클릭 시 현재 액티비티 종료
        binding.songNuguBtnDownIv.setOnClickListener {
            finish()
        }

        // 재생 및 일시정지 버튼 기능 설정
        binding.songBtnPlayIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songBtnPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        // 반복재생 버튼 기능 설정
        binding.songBtnRepeatedInactiveIv.setOnClickListener {
            setRepeatStatus(false)
        }
        binding.songBtnRepeatedActiveIv.setOnClickListener {
            setRepeatStatus(true)
        }

        // 랜덤재생 버튼 기능 설정
        binding.songBtnRandomInactiveIv.setOnClickListener {
            setRandomStatus(false)
        }
        binding.songBtnRandomActiveIv.setOnClickListener {
            setRandomStatus(true)
        }
    }

    // 사용자가 화면을 벗어났을 때 실행
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)

        // 현재까지 재생된 시간 반영
        song.second = ((binding.songProgreeSb.progress * song.playTime) / 100) / 1000

        // 내부 저장소에 데이터 저장
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val songJson = gson.toJson(song)
        editor.putString("songData", songJson)

        editor.apply()
    }

    // 액티비티 소멸 시 실행
    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어플레이어가 사용한 리소스 해제
        mediaPlayer = null
    }

    // 노래 초기화 함수
    private fun initSong() {
        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false),
                intent.getBooleanExtra("isRepeated", false),
                intent.getStringExtra("music")!!
            )
        }
        startTimer()
    }

    // 플레이어 설정 함수
    private fun setPlayer(song: Song) {
        binding.songTitleTv.text = intent.getStringExtra("title")
        binding.songSingerTv.text = intent.getStringExtra("singer")
        binding.songPlayStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songPlayEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songProgreeSb.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        setPlayerStatus(song.isPlaying)
    }

    // 플레이어의 상태(재생 또는 일시정지) 설정
    private fun setPlayerStatus(isPlaying: Boolean) {
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (isPlaying) {
            startMediaPlayer()
            Log.d("play", "재생버튼클릭")
            updatePlayPauseButtonVisibility(View.GONE, View.VISIBLE)
        } else {
            Log.d("pause", "멈춤버튼클릭")
            updatePlayPauseButtonVisibility(View.VISIBLE, View.GONE)
            pauseMediaPlayer()
        }
    }

    private fun startMediaPlayer() {
        mediaPlayer?.start()
    }

    private fun pauseMediaPlayer() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    private fun updatePlayPauseButtonVisibility(playVisibility: Int, pauseVisibility: Int) {
        binding.songBtnPlayIv.visibility = playVisibility
        binding.songBtnPauseIv.visibility = pauseVisibility
    }

    // 반복재생 버튼의 상태 설정
    private fun setRepeatStatus(isRepeat: Boolean) {
        song.isRepeated = isRepeat

        if (isRepeat) {
            Log.d("norepeat", "반복재생 꺼짐")
            updateRepeatButtonVisibility(View.VISIBLE, View.GONE)
        } else {
            Log.d("yesrepeat", "반복재생 켜짐")
            updateRepeatButtonVisibility(View.GONE, View.VISIBLE)
        }
    }

    private fun updateRepeatButtonVisibility(inactiveVisibility: Int, activeVisibility: Int) {
        binding.songBtnRepeatedInactiveIv.visibility = inactiveVisibility
        binding.songBtnRepeatedActiveIv.visibility = activeVisibility
    }

    // 랜덤재생 버튼의 상태 설정
    private fun setRandomStatus(isRandom: Boolean) {
        if (isRandom) {
            binding.songBtnRandomInactiveIv.visibility = View.VISIBLE
            binding.songBtnRandomActiveIv.visibility = View.GONE
        } else {
            binding.songBtnRandomInactiveIv.visibility = View.GONE
            binding.songBtnRandomActiveIv.visibility = View.VISIBLE
        }
    }

    // 타이머 시작 함수
    private fun startTimer() {
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    // 내부 클래스 - 타이머 설정
    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {
        private var second: Int = 0
        private var mills: Float = 0f
        private var isTimerRunning = true // 플래그 변수: 타이머 동작 여부를 제어

        override fun run() {
            super.run()
            try {
                while (isTimerRunning) {
                    // 한 곡이 종료되면
                    if (song.isPlaying && second >= playTime) {
                        // 반복재생이 설정된 경우, 다시 재생
                        if (!song.isRepeated) {
                            Log.d("repeat","반복재생")
                            initSong()
                            mediaPlayer?.seekTo(0)
                            second = 0
                            mills = 0f
                            mediaPlayer?.start()
                        } else {
                            Log.d("norepeat","반복재생안함")
                            // 반복재생이 설정되지 않은 경우, 종료
                            runOnUiThread {
                                setPlayerStatus(false)
                            }
                            mediaPlayer?.seekTo(0)
                            initSong()
                            second = 0
                            mills = 0f
                            isTimerRunning = false // 타이머 중지
                        }
                    }

                    if (isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgreeSb.progress = ((mills / playTime) * 100).toInt()
                            binding.songPlayStartTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)
                        }
                        if (mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.songPlayStartTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            } catch (e: InterruptedException) {
                d("song", "스레드 죽음 ㅠㅠ ${e.message}")
            }
        }

        // Timer를 안전하게 중지하는 메소드 추가
        fun stopTimer() {
            isTimerRunning = false
        }
    }
}
