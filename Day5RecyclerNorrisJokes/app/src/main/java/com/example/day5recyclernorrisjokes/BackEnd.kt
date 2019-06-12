package com.example.day5recyclernorrisjokes

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.Html
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class BackEnd : ViewModel() {

    companion object {
        var urlOfJsonJokes = "https://api.icndb.com/jokes/random"
        var client = OkHttpClient()
    }

    private var isLoadingState = MutableLiveData<Boolean>()
    fun getLoadingState(): LiveData<Boolean> = isLoadingState

    private var liveJokeText = MutableLiveData<String>()
    fun getLiveJokeText() :LiveData<String> = liveJokeText

    fun updateJokeText(){
        this.isLoadingState.postValue(true)

        val request = Request.Builder()
            .url(urlOfJsonJokes)
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                liveJokeText.postValue("Error: Api don`t response!")
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val json = JSONObject(response.body()?.string())
                    val jokeText = json.getJSONObject("value").getString("joke")
                    liveJokeText.postValue(Html.fromHtml(jokeText).toString())
                    isLoadingState.postValue(false)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

        })

    }

}