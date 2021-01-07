package com.septian.githubuserfinal.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.septian.githubuserfinal.BuildConfig
import com.septian.githubuserfinal.dataclass.User
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel : ViewModel() {
    companion object {
        private val TAG = DetailViewModel::class.java.simpleName
    }

    private val apiKey = BuildConfig.GITHUB_TOKEN
    private val listDetail = MutableLiveData<User>()

    fun setDetailUser(login: String?) {
        val client = AsyncHttpClient()

        val url = "https://api.github.com/users/$login"
        client.addHeader("Authorization", "token $apiKey")
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val userDetail = User()

                    userDetail.id = responseObject.getInt("id")
                    userDetail.login = responseObject.getString("login")
                    userDetail.name = responseObject.getString("name")
                    userDetail.html = responseObject.getString("html_url")
                    userDetail.avatar = responseObject.getString("avatar_url")
                    userDetail.location = responseObject.getString("location")
                    userDetail.repository = responseObject.getInt("public_repos")
                    userDetail.company = responseObject.getString("company")
                    userDetail.followers = responseObject.getInt("followers")
                    userDetail.following = responseObject.getInt("following")

                    listDetail.postValue(userDetail)

                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Log.d(TAG, errorMessage)
            }

        })
    }

    fun getDetailUser(): LiveData<User> {
        return listDetail
    }
}