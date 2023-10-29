package com.example.flo

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson
import java.util.Timer
class SongActivity : AppCompatActivity(){
    // 전역변수
    lateinit var binding : ActivitySongBinding
    lateinit var song : Song
    lateinit var timer: Timer

    // media player '?'
    private var mediaPlayer: MediaPlayer? = null

    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater) // xml의 레이아웃을 메모리에 객체화시키는 것
        setContentView(binding.root) // activity_song.xml

        initSong()
        setPlayer(song)

        binding.songNuguBtnDownIv.setOnClickListener{
            finish()
        }

        // 재생 버튼
        binding.songBtnPlayIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songBtnPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        // 반복재생 버튼
        binding.songBtnRepeatedInactiveIv.setOnClickListener {
            setRepeatStatus(false)
        }
        binding.songBtnRepeatedActiveIv.setOnClickListener {
            setRepeatStatus(true)
        }

        // 랜덤재생 버튼
        binding.songBtnRandomInactiveIv.setOnClickListener {
            setRandomStatus(false)
        }
        binding.songBtnRandomActiveIv.setOnClickListener {
            setRandomStatus(true)
        }
    }

    // 사용자가 포커스를 잃었을 때 음악 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)

        // 몇초까지 재생되고 있었는지 반영
        song.second = ((binding.songProgreeSb.progress * song.playTime)/100)/1000

        // 내부 저장소에 데이터 저장
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() // 에디터
        // song의 객체를 넘겨줘야함
        // editor.putString("title", song.title)
        // 이렇게 하면 너무 번거로움
        // 따라서 json 파일 사용할 것!
        val songJson = gson.toJson(song)
        editor.putString("songData", songJson)

        editor.apply() // 실제 저장공간에 저장됨
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어플레이어 해제
    }

    private fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false),
                intent.getStringExtra("music")!!
            )
        }
        startTimer()
    }

    private fun setPlayer(song: Song){
        binding.songTitleTv.text=intent.getStringExtra("title")
        binding.songSingerTv.text=intent.getStringExtra("singer")
        binding.songPlayStartTimeTv.text=String.format("%02d:%02d", song.second/60, song.second%60)
        binding.songPlayEndTimeTv.text=String.format("%02d:%02d", song.playTime/60, song.playTime%60)
        binding.songProgreeSb.progress=(song.second * 1000 / song.playTime)

        // resource를 받음
        val music = resources.getIdentifier(song.music, "raw", this.packageName)

        // mediaPlayer에게 넘겨줌
        mediaPlayer = MediaPlayer.create(this, music)

        setPlayerStatus(song.isPlaying)
    }

    // 버튼을 통해 재생, 멈춤을 관리
    fun setPlayerStatus(isPlaying : Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.songBtnPlayIv.visibility = View.GONE
            binding.songBtnPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        }
        else{
            binding.songBtnPlayIv.visibility = View.VISIBLE
            binding.songBtnPauseIv.visibility = View.GONE
            // 재생중이 아닐 때 pause()를 하면 오류가 생길 수 있으므로 조건문 추가
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }
    }

    fun setRepeatStatus(isRepeat : Boolean){
        if(isRepeat){
            binding.songBtnRepeatedInactiveIv.visibility = View.VISIBLE
            binding.songBtnRepeatedActiveIv.visibility = View.GONE
        }
        else{
            binding.songBtnRepeatedInactiveIv.visibility = View.GONE
            binding.songBtnRepeatedActiveIv.visibility = View.VISIBLE
        }
    }

    fun setRandomStatus(isRandom : Boolean){
        if(isRandom){
            binding.songBtnRandomInactiveIv.visibility = View.VISIBLE
            binding.songBtnRandomActiveIv.visibility = View.GONE
        }
        else{
            binding.songBtnRandomInactiveIv.visibility = View.GONE
            binding.songBtnRandomActiveIv.visibility = View.VISIBLE
        }
    }

    private fun startTimer(){
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true):Thread(){
        private var second : Int = 0
        private var mills: Float = 0f

        override fun run(){
            super.run()
            try {
                while(true){
                    if(second >= playTime){
                        second = 0;
                        mills = 0f;
                        break
                    }

                    if(isPlaying){
                        sleep(50)
                        mills += 50

                        runOnUiThread{
                            binding.songProgreeSb.progress = ((mills / playTime)*100).toInt()
                        }
                        if(mills % 1000 == 0f){
                            runOnUiThread{
                                binding.songPlayStartTimeTv.text=String.format("%02d:%02d", second/60, second%60)
                            }
                            second++
                        }
                    }
                }
            }catch (e: InterruptedException){
                Log.d("song", "스레드 죽음 ㅠㅠ ${e.message}")
            }
        }
    }
}