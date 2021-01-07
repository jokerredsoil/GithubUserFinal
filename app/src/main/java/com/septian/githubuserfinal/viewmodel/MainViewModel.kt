package com.septian.githubuserfinal.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpClient.log
import com.loopj.android.http.AsyncHttpResponseHandler
import com.septian.githubuserfinal.BuildConfig
import com.septian.githubuserfinal.dataclass.User
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel : ViewModel() {
    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    private val apiKey = BuildConfig.GITHUB_TOKEN
    val listSearch = MutableLiveData<ArrayList<User>>()

    fun setSearchUser(username: String) {
        val list = ArrayList<User>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
        client.addHeader("Authorization", "token $apiKey")
        client.addHeader("User-Agent", "request")
        Log.e(TAG, username)
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                try {
                    val result = String(responseBody!!)
                    val responseJSONObject = JSONObject(result)
                    val items = responseJSONObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val user = User()
                        user.id = item.getInt("id")
                        user.login = item.getString("login")
                        user.html = item.getString("html_url")
                        user.avatar = item.getString("avatar_url")
                        list.add(user)
                    }
                    listSearch.postValue(list)
                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                log.d(TAG, error?.message.toString())
            }
        })
    }

    fun getSearchUser(): LiveData<ArrayList<User>> {
        return listSearch
    }
}