package info.igorek.practice

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import info.igorek.practice.contentprovider.MusicContentProvider.Companion.COLUMN_ARTIST
import info.igorek.practice.contentprovider.MusicContentProvider.Companion.COLUMN_GENRE
import info.igorek.practice.contentprovider.MusicContentProvider.Companion.COLUMN_ID
import info.igorek.practice.contentprovider.MusicContentProvider.Companion.COLUMN_PATH
import info.igorek.practice.contentprovider.MusicContentProvider.Companion.COLUMN_TITLE
import info.igorek.practice.contentprovider.MusicContentProvider.Companion.CONTENT_URI
import info.igorek.practice.contentprovider.Song
import info.igorek.practice.contentprovider.SongAdapter
import info.igorek.practice.databinding.ActivitySelectArtistBinding

class SelectArtistActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectArtistBinding
    private lateinit var localBroadcastManager: LocalBroadcastManager

    private val chooseArtist by lazy {
        resources.getString(R.string.choose_artist)
    }
    private val chooseGenre by lazy {
        resources.getString(R.string.choose_genre)
    }

    private val songAdapter = SongAdapter { title, artist, genre, path ->
        this.sendBroadcast(
            Intent(ACTION_SONG_CHANGED).apply {
                putExtra(EXTRA_SONG_NAME, title)
                putExtra(EXTRA_ARTIST_NAME, artist)
                putExtra(EXTRA_GENRE_NAME, genre)
                putExtra(EXTRA_SONG_PATH, path)

            }
        )
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectArtistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        localBroadcastManager = LocalBroadcastManager.getInstance(this)

        val artistGenreList = getArtistsAndGenres()

        val artistList = artistGenreList.map { it.first }.distinct().toMutableList().apply {
            add(0, chooseArtist)
        }

        val genreList = artistGenreList.map { it.second }.distinct().toMutableList().apply {
            add(0, chooseGenre)
        }

        with(binding) {
            spinnerArtist.apply {
                adapter = ArrayAdapter(
                    this@SelectArtistActivity,
                    android.R.layout.simple_spinner_item,
                    artistList
                )
                onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        getByArtistAndGenre()
                    }
                }
            }

            spinnerGenre.apply {
                adapter = ArrayAdapter(
                    this@SelectArtistActivity,
                    android.R.layout.simple_spinner_item,
                    genreList
                )

                onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        getByArtistAndGenre()
                    }

                }
            }

            recyclerviewSongs.adapter = songAdapter
        }
    }

    private fun getArtistsAndGenres(): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()

        val cursor = contentResolver.query(CONTENT_URI, arrayOf(COLUMN_ARTIST, COLUMN_GENRE), null, null, null)
        cursor?.use { c ->
            while (c.moveToNext()) {
                list.add(
                    Pair(
                        c.getString(c.getColumnIndexOrThrow(COLUMN_ARTIST)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_GENRE)),
                    )
                )
            }
        }
        return list
    }

    private fun getByArtistAndGenre() {
        val songList = mutableListOf<Song>()

        val artist = binding.spinnerArtist.selectedItem.toString()
        val genre = binding.spinnerGenre.selectedItem.toString()

        val selection = when {
            artist != chooseArtist && genre != chooseGenre -> {
                "$COLUMN_ARTIST = \"$artist\" AND $COLUMN_GENRE = \"$genre\""
            }

            artist == chooseArtist && genre != chooseGenre -> {
                "$COLUMN_GENRE = \"$genre\""
            }

            artist != chooseArtist && genre == chooseGenre -> {
                "$COLUMN_ARTIST = \"$artist\""
            }

            else -> null
        }

        val cursor2 = contentResolver.query(
            CONTENT_URI,
            null,
            selection,
            null,
            null,
        )

        cursor2?.use { c ->
            while (c.moveToNext()) {
                songList.add(
                    Song(
                        c.getLong(c.getColumnIndexOrThrow(COLUMN_ID)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_ARTIST)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_GENRE)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_PATH)),
                    )
                )
            }
        }
        songAdapter.submitList(songList)
    }
}
