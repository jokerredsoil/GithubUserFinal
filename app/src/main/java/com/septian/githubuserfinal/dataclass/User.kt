package com.septian.githubuserfinal.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
        var id: Int = 0,
        var login: String? = null,
        var name: String? = null,
        var html: String? = null,
        var avatar: String? = null,
        var location: String? = null,
        var repository: Int = 0,
        var company: String? = null,
        var followers: Int = 0,
        var following: Int = 0
) : Parcelable
