package com.example.musicpie

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.example.musicpie.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var playPauseButton: Button
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var seekBar: SeekBar
    private lateinit var cover: ImageView
    private var pos = 0

    private var mediaPlayer: MediaPlayer? = null
    private var covers: MutableList<Int> = mutableListOf(
        R.drawable.cover1,
        R.drawable.cover2,
        R.drawable.cover3
    )
    private var songs: MutableList<Int> = mutableListOf(
        R.raw.song1,
        R.raw.song2,
        R.raw.song3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        playPauseButton = binding.playPauseButton
        nextButton = binding.nextButton
        previousButton = binding.previousButton
        seekBar = binding.seekBar
        cover = binding.cover

        player(songs[pos])
        cover.setImageResource(covers[0])
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

    private fun player(id: Int) {
        playPauseButton.setOnClickListener {
            if (mediaPlayer == null) {
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
            playPauseButton.setBackgroundResource(R.drawable.pause)
            cover.setImageResource(covers[pos])
        }
    }
}
