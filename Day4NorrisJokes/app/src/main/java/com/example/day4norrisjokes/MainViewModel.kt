package com.example.day4norrisjokes

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

class MainViewModel(var data : MainData) : ViewModel() {

    private var isLoadingState = MutableLiveData<Boolean>()
    private var liveJokeText = MutableLiveData<String>()
    var liveJokeImgUrl = MutableLiveData<String>()
    init {
        isLoadingState.postValue(false)
        data.getLoadingState().observeForever {
            isLoadingState.postValue(it)
//            println("in ViewModel isLoadingState is changed")
        }
        data.getLiveJokeText().observeForever {
            liveJokeText.postValue(it)
//            println("in ViewModel liveJokeText is changed")
        }
        data.getLiveJokeImgUrl().observeForever {
            liveJokeImgUrl.postValue(it)
//            println("in ViewModel liveJokeImgUrl is changed")
        }
    }

    fun getLoadingState(): LiveData<Boolean> = isLoadingState

    fun getLiveJokeText() :LiveData<String> = liveJokeText

    fun getLiveJokeImgUrl() :LiveData<String> = liveJokeImgUrl

    fun setNewJoke(){
        data.getNewLiveJokeText()
        data.getNewLiveJokeImgUrl()
    }

}

