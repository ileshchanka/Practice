package info.igorek.practice

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import info.igorek.practice.contentprovider.SongAdapter
import info.igorek.practice.databinding.ActivitySelectArtistBinding

class SelectArtistActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectArtistBinding
    private lateinit var viewModel: ViewModel
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

        viewModel = ViewModelProvider(this).get(ViewModel::class.java)

        localBroadcastManager = LocalBroadcastManager.getInstance(this)

        val artistGenreList = viewModel.getArtistsAndGenres(contentResolver)

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
                        songAdapter.submitList(
                            viewModel.getByArtistAndGenre(
                                contentResolver = contentResolver,
                                selectedArtist = binding.spinnerArtist.selectedItem.toString(),
                                selectedGenre = binding.spinnerGenre.selectedItem.toString(),
                                defaultArtist = chooseArtist,
                                defaultGenre = chooseGenre,
                            )
                        )
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
                        songAdapter.submitList(
                            viewModel.getByArtistAndGenre(
                                contentResolver = contentResolver,
                                selectedArtist = binding.spinnerArtist.selectedItem.toString(),
                                selectedGenre = binding.spinnerGenre.selectedItem.toString(),
                                defaultArtist = chooseArtist,
                                defaultGenre = chooseGenre,
                            )
                        )
                    }

                }
            }

            recyclerviewSongs.adapter = songAdapter
        }
    }
}
