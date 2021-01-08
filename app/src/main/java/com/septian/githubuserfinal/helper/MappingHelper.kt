package com.septian.githubuserfinal.helper

import android.database.Cursor
import android.util.Log
import com.septian.githubuserfinal.dataclass.User
import com.septian.githubuserfinal.db.DatabaseContract

object MappingHelper {
    private val TAG = MappingHelper::class.java.simpleName
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<User> {
        val list = ArrayList<User>()
        cursor?.apply {
            while (moveToNext()) {

                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val html = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.HTML))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))
                list.add(User(id,login,null,html,avatar))
                Log.d(TAG, cursor.toString())
            }
        }
        return list
    }
}