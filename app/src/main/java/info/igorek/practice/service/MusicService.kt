package info.igorek.practice.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener
import com.google.android.exoplayer2.util.NotificationUtil.IMPORTANCE_HIGH
import info.igorek.practice.ACTION_PAUSE
import info.igorek.practice.ACTION_STOP
import info.igorek.practice.EXTRA_ARTIST_NAME
import info.igorek.practice.EXTRA_SONG_NAME
import info.igorek.practice.EXTRA_SONG_PATH
import info.igorek.practice.MainActivity
import info.igorek.practice.R

class MusicService : Service() {

    companion object {
        private const val NOTIFICATION_ID = 123
        private const val CHANNEL_ID = "channel_id"
    }

    private lateinit var player: ExoPlayer
    private lateinit var notificationManager: PlayerNotificationManager
    private lateinit var notificationTitle: String

    private val descriptionAdapter = object : MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return notificationTitle
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            val openApp = Intent(applicationContext, MainActivity::class.java)
            return PendingIntent.getActivity(
                applicationContext,
                0,
                openApp,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
            )
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            return player.currentMediaItem?.mediaMetadata?.description
        }

        override fun getCurrentLargeIcon(player: Player, callback: PlayerNotificationManager.BitmapCallback): Bitmap? {
            return null
        }
    }

    private val notificationListener = object : NotificationListener {
        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            stopSelf()
            super.onNotificationCancelled(notificationId, dismissedByUser)
        }

        override fun onNotificationPosted(notificationId: Int, notification: Notification, ongoing: Boolean) {
            startForeground(notificationId, notification)
            super.onNotificationPosted(notificationId, notification, ongoing)
        }
    }

    override fun onCreate() {
        super.onCreate()

        player = ExoPlayer.Builder(this).build()

        notificationManager = PlayerNotificationManager.Builder(
            this,
            NOTIFICATION_ID,
            CHANNEL_ID,
        )
            .setChannelImportance(IMPORTANCE_HIGH)
            .setMediaDescriptionAdapter(descriptionAdapter)
            .setNotificationListener(notificationListener)
            .setChannelDescriptionResourceId(R.string.channel_desc)
            .setChannelNameResourceId(R.string.channel_name)
            .build()

        notificationManager.apply {
            setPlayer(player)
            setPriority(NotificationCompat.PRIORITY_MAX)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            ACTION_PAUSE -> {
                if (player.isPlaying) {
                    player.pause()
                } else {
                    player.play()
                }
            }

            ACTION_STOP -> {
                player.stop()
                player.seekTo(0)
                stopForeground(true)
                stopSelf()
            }

            else -> {
                notificationTitle =
                    "${intent?.getStringExtra(EXTRA_ARTIST_NAME)} – ${intent?.getStringExtra(EXTRA_SONG_NAME)}"
                val mediaItem = MediaItem.fromUri(Uri.parse(intent?.getStringExtra(EXTRA_SONG_PATH)))

                player.apply {
                    setMediaItem(mediaItem)
                    prepare()
                    playWhenReady = true
                }
            }
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        notificationManager.setPlayer(null)
        player.release()
        super.onDestroy()
    }
}