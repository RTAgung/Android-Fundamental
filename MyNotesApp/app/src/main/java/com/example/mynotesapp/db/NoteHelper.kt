package com.example.mynotesapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.example.mynotesapp.db.DatabaseContract.NoteColumns

class NoteHelper(context: Context) {
    companion object {
        private const val DATABASE_TABLE = NoteColumns.TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private lateinit var database: SQLiteDatabase

        private var INSTANCE: NoteHelper? = null
        fun getInstance(context: Context): NoteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NoteHelper(context)
            }
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLiteException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor =
        database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "${NoteColumns._ID} ASC"
        )

    fun queryById(id: String): Cursor =
        database.query(
            DATABASE_TABLE,
            null,
            "${NoteColumns._ID} = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )

    fun insert(values: ContentValues?) : Long =
        database.insert(DATABASE_TABLE, null, values)

    fun update(id: String, values: ContentValues?): Int =
        database.update(DATABASE_TABLE, values, "${NoteColumns._ID} = ?", arrayOf(id))

    fun deleteById(id: String): Int =
        database.delete(DATABASE_TABLE, "${NoteColumns._ID} = $id", null)
}