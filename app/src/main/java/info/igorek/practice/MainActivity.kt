package info.igorek.practice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.exoplayer2.util.Util
import info.igorek.practice.databinding.ActivityMainBinding
import info.igorek.practice.service.MusicService

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var songChangeReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var songName = EMPTY_STRING
        var artistName = EMPTY_STRING
        var songPath = EMPTY_STRING

        with(binding) {
            buttonPlay.setOnClickListener {
                if (songPath.isNotEmpty()) {
                    Util.startForegroundService(
                        this@MainActivity,
                        Intent(this@MainActivity, MusicService::class.java).apply {
                            putExtra(EXTRA_SONG_NAME, songName)
                            putExtra(EXTRA_ARTIST_NAME, artistName)
                            putExtra(EXTRA_SONG_PATH, songPath)
                        }
                    )
                }
            }

            buttonPause.setOnClickListener {
                startService(
                    Intent(this@MainActivity, MusicService::class.java).apply {
                        action = ACTION_PAUSE
                    }
                )
            }
            buttonStop.setOnClickListener {
                startService(
                    Intent(this@MainActivity, MusicService::class.java).apply {
                        action = ACTION_STOP
                    }
                )
            }

            buttonSelectArtist.setOnClickListener {
                startActivity(
                    Intent(this@MainActivity, SelectArtistActivity::class.java)
                )
            }
        }

        songChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                songName = intent.getStringExtra(EXTRA_SONG_NAME).orEmpty()
                artistName = intent.getStringExtra(EXTRA_ARTIST_NAME).orEmpty()
                songPath = intent.getStringExtra(EXTRA_SONG_PATH).orEmpty()
                with(binding) {
                    textviewSongName.text = intent.getStringExtra(EXTRA_SONG_NAME)
                    textviewSongArtist.text = intent.getStringExtra(EXTRA_ARTIST_NAME)
                    textviewSongGenre.text = intent.getStringExtra(EXTRA_GENRE_NAME)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        IntentFilter(ACTION_SONG_CHANGED).also {
            registerReceiver(songChangeReceiver, it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        LocalBroadcastManager.getInstance(this).unregisterReceiver(songChangeReceiver)
    }
}