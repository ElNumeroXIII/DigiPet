package com.example.digipet

import androidx.lifecycle.ViewModel

class MediaViewModel : ViewModel() {
    // Referencia al archivo de video en res/raw (por ejemplo, video.mp4)
    val videoResId: Int = R.raw.videoplayback

    // Referencia al archivo de audio en res/raw (por ejemplo, audio.mp3)
    val audioResId: Int = R.raw.digivolvemyheart
}

