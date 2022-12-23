package com.application.firebaseoperations

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import kotlinx.coroutines.flow.flow

class MediaPlayerFlow {
    private val mediaPlayer = MediaPlayer()
    private var currentPosition = 0

    fun play(context: Context, uri: Uri) = flow {
        mediaPlayer.setDataSource(context, uri)
        mediaPlayer.prepare()
        mediaPlayer.start()
        emit(Unit)
    }

    fun pause() {
        currentPosition = mediaPlayer.currentPosition
        mediaPlayer.pause()
    }

    fun stop() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        currentPosition = 0
    }

    fun resume() {
        mediaPlayer.seekTo(currentPosition)
        mediaPlayer.start()
    }

    fun isPlaying() = mediaPlayer.isPlaying
}