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
import org.json.JSONArray

class FollowerViewModel : ViewModel() {

    val listFollowerViewModel = MutableLiveData<ArrayList<User>>()
    private val apiKey = BuildConfig.GITHUB_TOKEN

    fun setFollower(loginName: String) {
        val list = ArrayList<User>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$loginName/followers"
        client.addHeader("Authorization", "token $apiKey")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
            ) {
                val result = String(responseBody!!)
                val responseArray = JSONArray(result)
                try {
                    for (i in 0 until responseArray.length()) {
                        val jsonObject = responseArray.getJSONObject(i)
                        val user = User()
                        user.login = jsonObject.getString("login")
                        user.avatar = jsonObject.getString("avatar_url")
                        user.html = jsonObject.getString("html_url")
                        list.add(user)
                    }
                    listFollowerViewModel.postValue(list)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
            }

        })
    }

    fun getFollowers(): LiveData<ArrayList<User>> {
        return listFollowerViewModel
    }
}