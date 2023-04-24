package com.example.musicpie

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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

    private var mediaPlayer: MediaPlayer? = null
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

        player(songs[pos])
        cover.setImageResource(covers[pos])
    }

    private fun startAnimation(motionLayout: MotionLayout) {
        Log.d("NOOOOOOO", "ID: ${mediaPlayer?.isPlaying}")
        motionLayout.transitionToEnd()
        motionLayout.transitionToStart()
    }

    private fun player(id: Int) {

        val motionLayout = findViewById<MotionLayout>(R.id.constraintLayout)

        playPauseButton.setOnClickListener {
            if (mediaPlayer == null) {
                startAnimation(motionLayout)
                mediaPlayer = MediaPlayer.create(this, id)
                mediaPlayer?.start()
                playPauseButton.setBackgroundResource(R.drawable.pause)
                cover.setImageResource(covers[pos])
                Log.d("PLAYING", "ID: ${mediaPlayer?.isPlaying}")
                initSeekbar()
            } else {
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.pause()
                    playPauseButton.setBackgroundResource(R.drawable.play)
                    Log.d("PAUSING", "ID: $id")
                } else {
                    startAnimation(motionLayout)
                    mediaPlayer?.start()
                    playPauseButton.setBackgroundResource(R.drawable.pause)
                    Log.d("PLAYING", "ID: $id")
                    initSeekbar()
                }
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, user: Boolean) {
                if (user)  mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        nextButton.setOnClickListener {
            mediaPlayer?.pause()

            if (pos < songs.size - 1) {
                pos++
            } else {
                pos = 0
            }

            mediaPlayer = MediaPlayer.create(this, songs[pos])
            mediaPlayer?.start()
            startAnimation(motionLayout)
            playPauseButton.setBackgroundResource(R.drawable.pause)
            cover.setImageResource(covers[pos])
        }

        previousButton.setOnClickListener {
            mediaPlayer?.pause()

            if (pos > 0) {
                pos--
            } else {
                pos = songs.size - 1
            }

            mediaPlayer = MediaPlayer.create(this, songs[pos])
            mediaPlayer?.start()
            startAnimation(motionLayout)
            playPauseButton.setBackgroundResource(R.drawable.pause)
            cover.setImageResource(covers[pos])
        }

        detailButton.setOnClickListener {
            val intent = Intent(this,DetailActivity::class.java)
            intent.putExtra("position", pos)
            mediaPlayer?.stop()
            mediaPlayer?.release()
            startActivity(intent)
        }
    }

    private fun initSeekbar() {

        seekBar.max = mediaPlayer?.duration ?: 0
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    seekBar.progress = mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    seekBar.progress = 0
                }
            }
        }, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }
}
