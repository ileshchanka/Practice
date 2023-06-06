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
        const val MIME_TYPE = "vnd.android.cursor.dir/vnd.example.music"
    }

    private lateinit var database: SQLiteDatabase

    override fun onCreate(): Boolean {
        val helper = DatabaseHelper(context!!)
        database = helper.writableDatabase
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
}
