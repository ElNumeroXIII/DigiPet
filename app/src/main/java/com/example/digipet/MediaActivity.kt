package com.example.digipet

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.digipet.databinding.ActivityMediaBinding

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding
    private val viewModel: MediaViewModel by viewModels()
    private var mediaPlayer: MediaPlayer? = null
    private var isAudioPlaying = false


    private val handler = Handler(Looper.getMainLooper())
    private lateinit var updateSeekBarRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tvVideoTitle.text = getString(R.string.video_title)
        binding.tvAudioTitle.text = getString(R.string.audio_title)

        val videoUri = Uri.parse("android.resource://$packageName/${viewModel.videoResId}")
        binding.videoView.setVideoURI(videoUri)


        binding.btnVideoPlay.setOnClickListener {
            if (binding.videoView.isPlaying) {
                binding.videoView.pause()
                binding.btnVideoPlay.visibility = View.VISIBLE
                binding.btnVideoPlay.setImageResource(R.drawable.baseline_play_circle_24)
            } else {
                binding.videoView.start()
                binding.btnVideoPlay.visibility = View.GONE
            }
        }

        // Listener para el contenedor del video
        binding.videoContainer.setOnClickListener {
            if (binding.videoView.isPlaying) {
                // Si el video se está reproduciendo y el botón está oculto, muéstralo con icono pause
                if (binding.btnVideoPlay.visibility == View.GONE) {
                    binding.btnVideoPlay.setImageResource(R.drawable.baseline_pause_circle_outline_24)
                    binding.btnVideoPlay.visibility = View.VISIBLE
                } else {
                    // Si ya se muestra, ocultarlo para ver el video sin obstrucciones
                    binding.btnVideoPlay.visibility = View.GONE
                }
            } else {
                // Si el video está pausado, mostrar el botón con icono play
                binding.btnVideoPlay.setImageResource(R.drawable.baseline_play_circle_24)
                binding.btnVideoPlay.visibility = View.VISIBLE
            }
        }

        // --- Configuración del audio local ---
        // Inicializar el MediaPlayer para el audio local usando el resource ID del ViewModel
        mediaPlayer = MediaPlayer.create(this, viewModel.audioResId)
        binding.seekBarAudio.max = mediaPlayer?.duration ?: 0

        // Runnable para actualizar la posición del SeekBar cada segundo
        updateSeekBarRunnable = object : Runnable {
            override fun run() {
                mediaPlayer?.let { mp ->
                    binding.seekBarAudio.progress = mp.currentPosition
                    handler.postDelayed(this, 1000)
                }
            }
        }
        handler.post(updateSeekBarRunnable)

        // Botón para reproducir/pausar el audio
        binding.btnAudioPlay.setOnClickListener {
            if (isAudioPlaying) {
                mediaPlayer?.pause()
                binding.btnAudioPlay.text = getString(R.string.play_audio)
            } else {
                mediaPlayer?.start()
                binding.btnAudioPlay.text = getString(R.string.pause_audio)
            }
            isAudioPlaying = !isAudioPlaying
        }

        // Botón para retroceder 5 segundos en el audio
        binding.btnRewind.setOnClickListener {
            mediaPlayer?.let { mp ->
                val newPos = (mp.currentPosition - 5000).coerceAtLeast(0)
                mp.seekTo(newPos)
                binding.seekBarAudio.progress = newPos
            }
        }

        // Botón para avanzar 5 segundos en el audio
        binding.btnForward.setOnClickListener {
            mediaPlayer?.let { mp ->
                val newPos = (mp.currentPosition + 5000).coerceAtMost(mp.duration)
                mp.seekTo(newPos)
                binding.seekBarAudio.progress = newPos
            }
        }

        // Permitir mover manualmente el SeekBar para el audio
        binding.seekBarAudio.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        binding.btnVolver.setOnClickListener {
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        handler.removeCallbacksAndMessages(null)
    }
}
