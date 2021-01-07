package com.septian.githubuserfinal.helper

import android.database.Cursor
import com.septian.githubuserfinal.db.DatabaseContract
import com.septian.githubuserfinal.dataclass.User

object MappingHelper {
    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<User> {
        val list = ArrayList<User>()

        userCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val username =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val html = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.HTML))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))
                list.add(User(id, username, null, html, avatar))
            }
        }
        return list
    }
}