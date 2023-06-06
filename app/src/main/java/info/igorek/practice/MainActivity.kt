package info.igorek.practice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.exoplayer2.util.Util
import info.igorek.practice.contentprovider.SelectArtistActivity
import info.igorek.practice.databinding.ActivityMainBinding
import info.igorek.practice.service.ForegroundService

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var songChangeReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        with(binding) {
            buttonStart.setOnClickListener {
                Util.startForegroundService(
                    this@MainActivity,
                    Intent(this@MainActivity, ForegroundService::class.java)
                )
            }
            buttonStop.setOnClickListener {

            }
            buttonSelectArtist.setOnClickListener {
                val intent = Intent(this@MainActivity, SelectArtistActivity::class.java)
                startActivity(intent)
                Log.d("_IHAR_", "startActivityForResult")
            }
        }

        songChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val songName = intent.getStringExtra(EXTRA_SONG_NAME)
                val artistName = intent.getStringExtra(EXTRA_ARTIST_NAME)
                val genreName = intent.getStringExtra(EXTRA_GENRE_NAME)

                binding.textviewSong.text = "$songName - $artistName\nGenre: $genreName"
                Log.d("_IHAR_", "MainActivity songChangeReceiver")
            }
        }

        IntentFilter(ACTION_SONG_CHANGED).also {
            registerReceiver(songChangeReceiver, it)
        }

        IntentFilter().also {
            registerReceiver(songChangeReceiver, it)
        }

    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(ACTION_SONG_CHANGED)
        LocalBroadcastManager.getInstance(this).registerReceiver(songChangeReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(songChangeReceiver)
    }
}