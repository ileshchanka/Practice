package info.igorek.practice.contentprovider

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import info.igorek.practice.ACTION_SONG_CHANGED
import info.igorek.practice.EXTRA_ARTIST_NAME
import info.igorek.practice.EXTRA_GENRE_NAME
import info.igorek.practice.EXTRA_SONG_NAME
import info.igorek.practice.databinding.ActivitySelectArtistBinding

class SelectArtistActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectArtistBinding

    private val artists = arrayOf("Artist 1", "Artist 2", "Artist 3")
    private val genres = arrayOf("Genre 1", "Genre 2", "Genre 3")

    private lateinit var localBroadcastManager: LocalBroadcastManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectArtistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("_IHAR_", "SelectArtistActivity onCreate")
        localBroadcastManager = LocalBroadcastManager.getInstance(this)

        val artistAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, artists)
        binding.spinnerArtist.adapter = artistAdapter

        val genreAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genres)
        binding.spinnerGenre.adapter = genreAdapter

        binding.buttonSelect.setOnClickListener {
            val selectedArtist = binding.spinnerArtist.selectedItem.toString()
            val selectedGenre = binding.spinnerGenre.selectedItem.toString()

            val intent = Intent(ACTION_SONG_CHANGED)
            intent.putExtra(EXTRA_SONG_NAME, "songName")
            intent.putExtra(EXTRA_ARTIST_NAME, selectedArtist)
            intent.putExtra(EXTRA_GENRE_NAME, selectedGenre)
            this.sendBroadcast(intent)
            Log.d("_IHAR_", "SelectArtistActivity buttonSelect.setOnClickListener")
            finish()
        }
    }
}
