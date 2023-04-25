package com.example.musicpie

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import com.example.musicpie.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var playPauseButton: Button
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var detailButton: Button
    private lateinit var seekBar: SeekBar
    private lateinit var cover: ImageView
    private var pos = 0

    private var mediaPlayerSingleton = MediaPlayerSingleton
    private var covers: MutableList<Int> = mutableListOf(
        R.drawable.cover1_circle,
        R.drawable.cover2_circle,
        R.drawable.cover3_circle
    )
    private var songs: MutableList<Int> = mutableListOf(
        R.raw.song1,
        R.raw.song2,
        R.raw.song3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pos = intent.getIntExtra("position", 0)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        playPauseButton = binding.playPauseButton
        nextButton = binding.nextButton
        previousButton = binding.previousButton
        detailButton = binding.detailButton
        seekBar = binding.seekBar
        cover = binding.cover

        mediaPlayerSingleton.mediaPlayer?.let {
            if (it.isPlaying) {
                playPauseButton.setBackgroundResource(R.drawable.pause)
            }
            initSeekbar()
        }
        player(songs[pos])
        cover.setImageResource(covers[pos])
    }

    private fun startAnimation(motionLayout: MotionLayout) {
        motionLayout.transitionToEnd()
        motionLayout.transitionToStart()
    }

    private fun player(id: Int) {

        val motionLayout = findViewById<MotionLayout>(R.id.constraintLayout)

        playPauseButton.setOnClickListener {

            if (mediaPlayerSingleton.isPlaying()) {
                mediaPlayerSingleton.pause()
                playPauseButton.setBackgroundResource(R.drawable.play)
            } else {
                if (mediaPlayerSingleton.mediaPlayer == null) {
                    mediaPlayerSingleton.init(this, id)
                }
                mediaPlayerSingleton.start()
                startAnimation(motionLayout)
                playPauseButton.setBackgroundResource(R.drawable.pause)
                initSeekbar()
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, user: Boolean) {
                if (user) mediaPlayerSingleton.mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        nextButton.setOnClickListener {
            mediaPlayerSingleton.mediaPlayer?.pause()

            if (pos < songs.size - 1) {
                pos++
            } else {
                pos = 0
            }

            mediaPlayerSingleton.mediaPlayer = MediaPlayer.create(this, songs[pos])
            mediaPlayerSingleton.mediaPlayer?.start()
            startAnimation(motionLayout)
            playPauseButton.setBackgroundResource(R.drawable.pause)
            cover.setImageResource(covers[pos])
        }

        previousButton.setOnClickListener {
            mediaPlayerSingleton.mediaPlayer?.pause()

            if (pos > 0) {
                pos--
            } else {
                pos = songs.size - 1
            }

            mediaPlayerSingleton.mediaPlayer = MediaPlayer.create(this, songs[pos])
            mediaPlayerSingleton.mediaPlayer?.start()
            startAnimation(motionLayout)
            playPauseButton.setBackgroundResource(R.drawable.pause)
            cover.setImageResource(covers[pos])
        }

        detailButton.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("position", pos)
            startActivity(intent)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerSingleton.mediaPlayer?.stop()
        mediaPlayerSingleton.release()
    }

    private fun initSeekbar() {

        seekBar.max = mediaPlayerSingleton.mediaPlayer?.duration ?: 0
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    seekBar.progress = mediaPlayerSingleton.mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    seekBar.progress = 0
                }
            }
        }, 0)
    }
}
