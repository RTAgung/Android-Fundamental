package com.example.mypreloaddata.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import com.example.mypreloaddata.database.DatabaseContract.TABLE_NAME
import com.example.mypreloaddata.database.DatabaseContract.MahasiswaColumns
import com.example.mypreloaddata.model.MahasiswaModel
import java.sql.SQLException

class MahasiswaHelper(context: Context) {

    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private var INSTANCE: MahasiswaHelper? = null
        fun getInstance(context: Context): MahasiswaHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = MahasiswaHelper(context)
                    }
                }
            }
            return INSTANCE as MahasiswaHelper
        }
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun getAllData(): ArrayList<MahasiswaModel> {
        val cursor = database.query(TABLE_NAME, null, null, null, null, null, "$_ID ASC", null)
        cursor.moveToFirst()
        val arrayList = ArrayList<MahasiswaModel>()
        var mahasiswaModel: MahasiswaModel
        if (cursor.count > 0) {
            do {
                mahasiswaModel = MahasiswaModel()
                mahasiswaModel.id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                mahasiswaModel.name =
                    cursor.getString(cursor.getColumnIndexOrThrow(MahasiswaColumns.NAMA))
                mahasiswaModel.nim =
                    cursor.getString(cursor.getColumnIndexOrThrow(MahasiswaColumns.NIM))

                arrayList.add(mahasiswaModel)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun insert(mahasiswaModel: MahasiswaModel): Long {
        val initialValues = ContentValues()
        initialValues.put(MahasiswaColumns.NAMA, mahasiswaModel.name)
        initialValues.put(MahasiswaColumns.NIM, mahasiswaModel.nim)
        return database.insert(TABLE_NAME, null, initialValues)
    }

    fun getDataByName(nama: String): ArrayList<MahasiswaModel> {
        val cursor = database.query(
            TABLE_NAME,
            null,
            "${MahasiswaColumns.NAMA} LIKE ?",
            arrayOf(nama),
            null,
            null,
            "$_ID ASC",
            null
        )
        cursor.moveToFirst()
        val arrayList = ArrayList<MahasiswaModel>()
        var mahasiswaModel: MahasiswaModel
        if (cursor.count > 0) {
            do {
                mahasiswaModel = MahasiswaModel()
                mahasiswaModel.id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                mahasiswaModel.name =
                    cursor.getString(cursor.getColumnIndexOrThrow(MahasiswaColumns.NAMA))
                mahasiswaModel.nim =
                    cursor.getString(cursor.getColumnIndexOrThrow(MahasiswaColumns.NIM))
                arrayList.add(mahasiswaModel)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun beginTransaction(){
        database.beginTransaction()
    }

    fun setTransactionSuccess(){
        database.setTransactionSuccessful()
    }

    fun endTransaction(){
        database.endTransaction()
    }

    fun insertTransaction(mahasiswaModel: MahasiswaModel){
        val sql = ("INSERT INTO $TABLE_NAME (${MahasiswaColumns.NAMA}, ${MahasiswaColumns.NIM}) VALUES (?, ?)")
        val stmt = database.compileStatement(sql)
        stmt.bindString(1, mahasiswaModel.name)
        stmt.bindString(2, mahasiswaModel.nim)
        stmt.execute()
        stmt.clearBindings()
    }
}
