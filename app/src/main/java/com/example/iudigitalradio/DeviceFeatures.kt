package com.example.iudigitalradio

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import java.io.File
import java.io.FileOutputStream

// ==================================================
// VIBRACIÓN DEL DISPOSITIVO
// ==================================================
fun vibrateDevice(context: Context) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(
                Context.VIBRATOR_MANAGER_SERVICE
            ) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(
            Context.VIBRATOR_SERVICE
        ) as Vibrator
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                1000L,255

            )
        )
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(200L)
    }
}


// ==================================================
// GUARDAR FOTOGRAFÍA DE PERFIL
// ==================================================
fun saveProfilePhoto(
    context: Context,
    bitmap: Bitmap
): Uri? {
    return try {
        val fileName =
            "profile_photo_${System.currentTimeMillis()}.jpg"
        val file =
            File(
                context.filesDir,
                fileName
            )
        FileOutputStream(file).use { outputStream ->

            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                90,
                outputStream
            )
        }
        Uri.fromFile(file)
    } catch (exception: Exception) {
        null
    }
}

// ==================================================
// REPRODUCTOR DE AUDIO
// ==================================================

object AudioPlayerManager {

    private var player: ExoPlayer? = null

    private var currentAudioResId: Int? = null


    // ==================================================
    // OBTENER REPRODUCTOR
    // ==================================================

    private fun getPlayer(
        context: Context
    ): ExoPlayer {

        if (player == null) {

            player =
                ExoPlayer
                    .Builder(
                        context.applicationContext
                    )
                    .build()
        }

        return player!!
    }


    // ==================================================
    // CARGAR EMISORA
    // ==================================================

    fun loadStation(
        context: Context,
        audioResId: Int
    ) {

        val exoPlayer =
            getPlayer(context)

        if (currentAudioResId == audioResId) {
            return
        }

        currentAudioResId =
            audioResId

        val mediaItem =
            MediaItem.fromUri(
                "android.resource://${context.packageName}/$audioResId"
            )

        exoPlayer.setMediaItem(
            mediaItem
        )

        exoPlayer.prepare()
    }


    // ==================================================
    // DETENER Y LIMPIAR AUDIO
    // ==================================================

    fun stop() {

        player?.stop()

        player?.clearMediaItems()

        currentAudioResId = null
    }


    // ==================================================
    // REPRODUCIR
    // ==================================================

    fun play(
        context: Context
    ) {

        getPlayer(context).play()
    }


    // ==================================================
    // PAUSAR
    // ==================================================

    fun pause(
        context: Context
    ) {

        getPlayer(context).pause()
    }


    // ==================================================
    // SILENCIAR
    // ==================================================

    fun setMuted(
        context: Context,
        muted: Boolean
    ) {

        getPlayer(context).volume =
            if (muted) {
                0f
            } else {
                1f
            }
    }


    // ==================================================
    // LIBERAR RECURSOS
    // ==================================================

    fun release() {

        player?.release()

        player = null

        currentAudioResId = null
    }
}
