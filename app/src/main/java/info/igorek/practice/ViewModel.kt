package info.igorek.practice

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import info.igorek.practice.contentprovider.MusicContentProvider
import info.igorek.practice.contentprovider.Song

class ViewModel : ViewModel() {

    fun getArtistsAndGenres(contentResolver: ContentResolver): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()

        val cursor = contentResolver.query(
            MusicContentProvider.CONTENT_URI, arrayOf(
                MusicContentProvider.COLUMN_ARTIST,
                MusicContentProvider.COLUMN_GENRE
            ), null, null, null
        )
        cursor?.use { c ->
            while (c.moveToNext()) {
                list.add(
                    Pair(
                        c.getString(c.getColumnIndexOrThrow(MusicContentProvider.COLUMN_ARTIST)),
                        c.getString(c.getColumnIndexOrThrow(MusicContentProvider.COLUMN_GENRE)),
                    )
                )
            }
        }
        return list
    }

    fun getByArtistAndGenre(
        contentResolver: ContentResolver,
        selectedArtist: String,
        selectedGenre: String,
        defaultArtist: String,
        defaultGenre: String,
    ): List<Song> {
        val songList = mutableListOf<Song>()

        val selection = when {
            selectedArtist != defaultArtist && selectedGenre != defaultGenre -> {
                "${MusicContentProvider.COLUMN_ARTIST} = \"$selectedArtist\" AND ${MusicContentProvider.COLUMN_GENRE} = \"$selectedGenre\""
            }

            selectedArtist == defaultArtist && selectedGenre != defaultGenre -> {
                "${MusicContentProvider.COLUMN_GENRE} = \"$selectedGenre\""
            }

            selectedArtist != defaultArtist && selectedGenre == defaultGenre -> {
                "${MusicContentProvider.COLUMN_ARTIST} = \"$selectedArtist\""
            }

            else -> null
        }

        val cursor2 = contentResolver.query(
            MusicContentProvider.CONTENT_URI,
            null,
            selection,
            null,
            null,
        )

        cursor2?.use { c ->
            while (c.moveToNext()) {
                songList.add(
                    Song(
                        c.getLong(c.getColumnIndexOrThrow(MusicContentProvider.COLUMN_ID)),
                        c.getString(c.getColumnIndexOrThrow(MusicContentProvider.COLUMN_ARTIST)),
                        c.getString(c.getColumnIndexOrThrow(MusicContentProvider.COLUMN_GENRE)),
                        c.getString(c.getColumnIndexOrThrow(MusicContentProvider.COLUMN_TITLE)),
                        c.getString(c.getColumnIndexOrThrow(MusicContentProvider.COLUMN_PATH)),
                    )
                )
            }
        }
        return songList
    }
}