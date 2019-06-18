package com.example.day4norrisjokes

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import kotlin.random.Random
import android.text.Html


class MainData {
    // хз як через потоки його норм синхронізувати, 4 часа сидів, забив на це і зделав через лайв дату
    private val isLoadingState = MutableLiveData<Boolean>()
    fun getLoadingState(): LiveData<Boolean> =
        isLoadingState

    private val liveJokeText = MutableLiveData<String>()
    fun getLiveJokeText(): LiveData<String> =
        liveJokeText

    private val liveJokeImgUrl = MutableLiveData<String>()
    fun getLiveJokeImgUrl(): LiveData<String> =
        liveJokeImgUrl
    fun getNewLiveJokeImgUrl(){
        this.liveJokeImgUrl.postValue(img_urls[Random.nextInt(img_urls.count())])
    }

    companion object {
        val img_urls = listOf<String>(
            "http://img2.rnkr-static.com/user_node_img/50016/1000312512/870/chuck-norris-chose-scissors-photo-u2.jpg",
            "https://s0.tchkcdn.com/i/9/1/33781_f826e84c_228862_23f3.jpeg",
            "https://ru.fenikssfun.com/cache/images/3582073834/1363205091_auto_prikoli_09_1_2984655367.jpg",
            "https://pp.userapi.com/c639217/v639217073/627db/XAFTVKMM_vg.jpg",
            "http://4fun.one/uploads/posts/t/l-1936.jpg",
            "http://tn.new.fishki.net/26/upload/post/201410/29/1322207/ezak1jjpw8s.jpg",
            "https://s5o.ru/storage/simple/ru/ugc/33/76/45/48/ruud4c6c5ec8c.jpg"
        )
        val urlOfJsonJokes = "https://api.icndb.com/jokes/random"
        val client = OkHttpClient()
    }

    fun getNewLiveJokeText(){
//        println("in getNewLiveJokeText")// executes order 1
        this.isLoadingState.postValue(true)

        val request = Request.Builder()
            .url(urlOfJsonJokes)
            .build()

        //val response = client.newCall(request).execute() // FATAL EXCEPTION: android.os.StrictMode$AndroidBlockGuardPolicy.onNetwork. WTF?!

        client.newCall(request).enqueue(object : Callback{
