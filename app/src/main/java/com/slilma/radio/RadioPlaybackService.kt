package com.slilma.radio

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class RadioPlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()
        val item = MediaItem.Builder()
            .setUri(RADIO_URL)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("SLILMA RADIO")
                    .setArtist("Transmisión en vivo")
                    .build()
            )
            .build()
        player.setMediaItem(item)
        player.prepare()
        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
        }
        mediaSession = null
        super.onDestroy()
    }

    companion object {
        const val RADIO_URL = "https://sonicpanel.zonaradio.net/8068/stream"
    }
}
