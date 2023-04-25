package com.example.musicpie

import android.content.Context
import android.media.MediaPlayer

object MediaPlayerSingleton {
    var mediaPlayer: MediaPlayer? = null

    fun init(context: Context, id: Int) {
        mediaPlayer = MediaPlayer.create(context, id)
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun start() {
        mediaPlayer?.start()
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }
}
