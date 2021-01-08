package com.septian.githubuserfinal.helper

import android.database.Cursor
import android.util.Log
import com.septian.githubuserfinal.dataclass.User
import com.septian.githubuserfinal.db.DatabaseContract

object MappingHelper {
    private val TAG = MappingHelper::class.java.simpleName
    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<User> {
        val list = ArrayList<User>()

        userCursor?.apply {
            while (moveToNext()) {
                val user = User()
                user.id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                user.login = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                user.html = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.HTML))
                user.avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))
                list.add(User())
                Log.d(TAG, user.toString())
            }
        }
        return list
    }
}