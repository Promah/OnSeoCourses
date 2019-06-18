package com.example.day6norrisrecyclenew

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import android.os.Message
import android.text.Html

import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import kotlin.random.Random


class BackEnd : ViewModel() {

    companion object {
        var JOKES_STACK_SIZE = 5
        var urlOfJsonJokes = "https://api.icndb.com/jokes/random"
        private val client = OkHttpClient()
        val img_urls = listOf<String>(
            "http://img2.rnkr-static.com/user_node_img/50016/1000312512/870/chuck-norris-chose-scissors-photo-u2.jpg",
            "https://s0.tchkcdn.com/i/9/1/33781_f826e84c_228862_23f3.jpeg",
            "https://ru.fenikssfun.com/cache/images/3582073834/1363205091_auto_prikoli_09_1_2984655367.jpg",
            "https://pp.userapi.com/c639217/v639217073/627db/XAFTVKMM_vg.jpg",
            "http://4fun.one/uploads/posts/t/l-1936.jpg",
            "http://tn.new.fishki.net/26/upload/post/201410/29/1322207/ezak1jjpw8s.jpg",
            "https://s5o.ru/storage/simple/ru/ugc/33/76/45/48/ruud4c6c5ec8c.jpg",
            "http://memesmix.net/media/created/nom9ko.jpg",
            "http://kcp.spb.ru/images/zhenschiny_velikogo_veka_benconi_zhyuletta_95154_1.jpg",
            "https://i.obozrevatel.com/gallery/2018/3/6/zarazilsya-eboloi-600x448.jpg",
            "https://i.obozrevatel.com/gallery/2018/3/6/chak-norris-3.jpg",
            "https://i.obozrevatel.com/gallery/2018/3/6/13789831291444783197-1.jpg"
        )
        private var isFirstJokesStackDownloaded = false
    }

    private val isDataLoading = MutableLiveData<Boolean>()
    fun getLoadingState(): LiveData<Boolean> =
        isDataLoading

    private val _jokesImgUrl = mutableListOf<String>()
    private val jokesImgUrl = MutableLiveData<List<String>>()
    fun getJokesImgUrl(): LiveData<List<String>> =
        jokesImgUrl

    private val _jokeTexts = mutableListOf<String>()
    private val jokeTexts = MutableLiveData<List<String>>()
    fun getJokeTexts(): LiveData<List<String>> =
        jokeTexts

    private var downloadedJokesCount = 0

    private val jokeStackDownloaderHandler = object : Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            msg?.let {
                if (downloadedJokesCount !=JOKES_STACK_SIZE){
                    downloadedJokesCount++
                    _jokeTexts.add(it.obj.toString())
                    _jokesImgUrl.add(img_urls[Random.nextInt(img_urls.count())])

                }else{
                    downloadedJokesCount = 0
                    _jokeTexts.add(it.obj.toString())
                    jokeTexts.postValue(_jokeTexts)
                    jokesImgUrl.postValue(_jokesImgUrl)
                    isDataLoading.postValue(false)
                }
            }
        }
    }

    private val downloadJokeTextRunnable = Runnable {
        try {
            val request = Request.Builder()
                .url(urlOfJsonJokes)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val mMessage = Message()
                    mMessage.obj = "api don`t response"
                    jokeStackDownloaderHandler.handleMessage(mMessage)
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = JSONObject(response.body()?.string())
                    val jokeText = JSONObject(json["value"].toString())["joke"] as String
                    val mMessage = Message()
                    mMessage.obj = Html.fromHtml(jokeText)
                    jokeStackDownloaderHandler.handleMessage(mMessage)
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getNewJokeTextsStack(){
        isDataLoading.postValue(true)
        for (i in 0..JOKES_STACK_SIZE)
            jokeStackDownloaderHandler.post(downloadJokeTextRunnable)
    }

    fun getFirstJokesTexts(){
        if (!isFirstJokesStackDownloaded){
            isFirstJokesStackDownloaded = true
            for (i in 0..JOKES_STACK_SIZE)
                jokeStackDownloaderHandler.post(downloadJokeTextRunnable)
            isDataLoading.postValue(true)
        }else{
            jokeTexts.postValue(listOf())
            jokesImgUrl.postValue(listOf())
        }
    }

}