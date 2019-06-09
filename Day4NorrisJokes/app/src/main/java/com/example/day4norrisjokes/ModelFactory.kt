package com.example.day4norrisjokes

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModel



class ModelFactory : ViewModelProvider.NewInstanceFactory() {

    val data = MainData()

    override fun <T : ViewModel> create(modelClass: Class<T?>): T {
        return when (modelClass){
            MainViewModel::class.java -> MainViewModel(data) as T
            else -> EmptyViewModel() as T
        }
    }
}