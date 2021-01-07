package com.septian.githubuserfinal.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.septian.githubuserfinal"
    const val SCHEME = "content"
    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val HTML = "html"
            const val AVATAR = "avatar"


            // create content://com.septian.githubuserfinal/user
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}