package com.slilma.radio

import android.Manifest
import android.content.ComponentName
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture

class MainActivity : AppCompatActivity() {
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var player: MediaController? = null
    private lateinit var playButton: Button
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        buildScreen()
        val token = SessionToken(this, ComponentName(this, RadioPlaybackService::class.java))
        controllerFuture = MediaController.Builder(this, token).buildAsync()
        controllerFuture?.addListener({
            try {
                player = controllerFuture?.get()
                player?.addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) = updateUi(isPlaying)
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        status.text = "No se pudo conectar. Verifica tu internet e inténtalo otra vez."
                        playButton.text = "REINTENTAR"
                    }
                })
                updateUi(player?.isPlaying == true)
            } catch (_: Exception) {
                status.text = "No se pudo iniciar el reproductor."
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun buildScreen() {
        val bg = Color.rgb(7, 21, 45)
        val cyan = Color.rgb(66, 214, 232)
        val white = Color.WHITE
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 36, 28, 36)
            setBackgroundColor(bg)
        }
        val live = TextView(this).apply {
            text = "●  TRANSMISIÓN EN VIVO"
            textSize = 14f
            setTextColor(cyan)
            gravity = Gravity.CENTER
        }
        val title = TextView(this).apply {
            text = "SLILMA\nRADIO"
            textSize = 42f
            setTextColor(white)
            gravity = Gravity.CENTER
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 30, 0, 12)
        }
        val subtitle = TextView(this).apply {
            text = "Tu compañía, tu música y tus mejores momentos."
            textSize = 16f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
        }
        status = TextView(this).apply {
            text = "Preparando la transmisión…"
            textSize = 14f
            setTextColor(cyan)
            gravity = Gravity.CENTER
            setPadding(0, 42, 0, 18)
        }
        playButton = Button(this).apply {
            text = "ESCUCHAR EN VIVO"
            textSize = 16f
            setOnClickListener {
                val p = player
                if (p == null) {
                    Toast.makeText(this@MainActivity, "Conectando… espera un momento.", Toast.LENGTH_SHORT).show()
                } else if (p.isPlaying) {
                    p.pause()
                } else {
                    p.prepare()
                    p.play()
                    status.text = "Conectando con SLILMA RADIO…"
                }
            }
        }
        val footer = TextView(this).apply {
            text = "Escúchanos desde cualquier lugar"
            textSize = 13f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 42, 0, 0)
        }
        layout.addView(live)
        layout.addView(title)
        layout.addView(subtitle)
        layout.addView(status)
        layout.addView(playButton)
        layout.addView(footer)
        setContentView(layout)
    }

    private fun updateUi(isPlaying: Boolean) {
        runOnUiThread {
            playButton.text = if (isPlaying) "PAUSAR TRANSMISIÓN" else "ESCUCHAR EN VIVO"
            status.text = if (isPlaying) "Estás escuchando SLILMA RADIO" else "Listos para transmitir"
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
        }
    }

    override fun onDestroy() {
        controllerFuture?.let { MediaController.releaseFuture(it) }
        player = null
        super.onDestroy()
    }
}
