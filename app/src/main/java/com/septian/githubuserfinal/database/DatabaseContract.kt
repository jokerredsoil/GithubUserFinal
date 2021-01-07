package com.septian.githubuserm.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.septian.githubuserm"
    const val SCHEME = "content"
    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "fav_user"
            const val _ID = "_id"
            const val COLUMN_NAME_USERNAME = "username"
            const val COLUMN_NAME_AVATAR_URL = "avatar_url"
            const val COLUMN_NAME_HTML_URL = "html_url"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}