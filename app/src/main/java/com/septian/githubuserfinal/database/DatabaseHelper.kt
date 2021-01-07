package com.septian.githubuserm.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.septian.githubuserm.database.DatabaseContract.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "dbfavuser"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME" +
                "(${DatabaseContract.UserColumns._ID} INTEGER PRIMARY KEY," +
                "(${DatabaseContract.UserColumns.COLUMN_NAME_USERNAME} TEXT NOT NULL," +
                "(${DatabaseContract.UserColumns.COLUMN_NAME_HTML_URL} TEXT NOT NULL,"+
                "(${DatabaseContract.UserColumns.COLUMN_NAME_AVATAR_URL} TEXT NOT NULL"

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}