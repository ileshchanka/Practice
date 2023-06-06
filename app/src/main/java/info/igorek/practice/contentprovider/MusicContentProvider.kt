package info.igorek.practice.contentprovider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri

class MusicContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "info.igorek.practice"
        const val DATABASE_NAME = "music.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "music"
        const val COLUMN_ID = "_id"
        const val COLUMN_ARTIST = "artist"
        const val COLUMN_GENRE = "genre"
        const val COLUMN_TITLE = "title"
        const val COLUMN_PATH = "path"
        const val MIME_TYPE = "vnd.android.cursor.dir/info.igorek.music"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    }

    private lateinit var database: SQLiteDatabase

    override fun onCreate(): Boolean {
        val helper = DatabaseHelper(context!!)
        database = helper.writableDatabase

        initValues()

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?,
    ): Cursor? {
        return database.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
    }

    override fun getType(uri: Uri): String? {
        return MIME_TYPE
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = database.insert(TABLE_NAME, null, values)
        return ContentUris.withAppendedId(uri, id)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return database.delete(TABLE_NAME, selection, selectionArgs)
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return database.update(TABLE_NAME, values, selection, selectionArgs)
    }

    private inner class DatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            val createTableQuery =
                "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_ARTIST TEXT, $COLUMN_GENRE TEXT, $COLUMN_TITLE TEXT, $COLUMN_PATH TEXT)"
            db.execSQL(createTableQuery)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }
    }

    private fun initValues() {

        database.execSQL("DELETE FROM $TABLE_NAME")

        insert(
            CONTENT_URI,
            ContentValues().apply {
                put(COLUMN_ARTIST, "Colorful Cat")
                put(COLUMN_GENRE, "Relax")
                put(COLUMN_TITLE, "Araishu")
                put(COLUMN_PATH, "android.resource://info.igorek.practice/raw/colorful_cat_araishu")
            }
        )
        insert(
            CONTENT_URI,
            ContentValues().apply {
                put(COLUMN_ARTIST, "Colorful Cat")
                put(COLUMN_GENRE, "Relax")
                put(COLUMN_TITLE, "Asagi")
                put(COLUMN_PATH, "android.resource://info.igorek.practice/raw/colorful_cat_asagi")
            }
        )
        insert(
            CONTENT_URI,
            ContentValues().apply {
                put(COLUMN_ARTIST, "Colorful Cat")
                put(COLUMN_GENRE, "Instrumental")
                put(COLUMN_TITLE, "Byakuroku")
                put(COLUMN_PATH, "android.resource://info.igorek.practice/raw/colorful_cat_byakuroku")
            }
        )
        insert(
            CONTENT_URI,
            ContentValues().apply {
                put(COLUMN_ARTIST, "David Cutter Music")
                put(COLUMN_GENRE, "Vlog")
                put(COLUMN_TITLE, "Ack Woi")
                put(COLUMN_PATH, "android.resource://info.igorek.practice/raw/david_cutter_music_ack_woi")
            }
        )
        insert(
            CONTENT_URI,
            ContentValues().apply {
                put(COLUMN_ARTIST, "Day 7")
                put(COLUMN_GENRE, "Relax")
                put(COLUMN_TITLE, "Cosmic Sailing")
                put(COLUMN_PATH, "android.resource://info.igorek.practice/raw/day_7_cosmic_sailing")
            }
        )
        insert(
            CONTENT_URI,
            ContentValues().apply {
                put(COLUMN_ARTIST, "Day 7")
                put(COLUMN_GENRE, "Instrumental")
                put(COLUMN_TITLE, "Dusk")
                put(COLUMN_PATH, "android.resource://info.igorek.practice/raw/day_7_dusk")
            }
        )
        insert(
            CONTENT_URI,
            ContentValues().apply {
                put(COLUMN_ARTIST, "Day 7")
                put(COLUMN_GENRE, "Relax")
                put(COLUMN_TITLE, "Waiting for you")
                put(COLUMN_PATH, "android.resource://info.igorek.practice/raw/day_7_waiting_for_you")
            }
        )
        insert(
            CONTENT_URI,
            ContentValues().apply {
                put(COLUMN_ARTIST, "Markvard")
                put(COLUMN_GENRE, "Indie")
                put(COLUMN_TITLE, "You And Me")
                put(COLUMN_PATH, "android.resource://info.igorek.practice/raw/markvard_you_and_me")
            }
        )
        insert(
            CONTENT_URI,
            ContentValues().apply {
                put(COLUMN_ARTIST, "Ooyy")
                put(COLUMN_GENRE, "Vlog")
                put(COLUMN_TITLE, "Maverick")
                put(COLUMN_PATH, "android.resource://info.igorek.practice/raw/ooyy_maverick")
            }
        )
        insert(
            CONTENT_URI,
            ContentValues().apply {
                put(COLUMN_ARTIST, "You Are Free")
                put(COLUMN_GENRE, "Relax")
                put(COLUMN_TITLE, "Never Let Go")
                put(COLUMN_PATH, "android.resource://info.igorek.practice/raw/you_are_free_never_let_go")
            }
        )
    }
}
