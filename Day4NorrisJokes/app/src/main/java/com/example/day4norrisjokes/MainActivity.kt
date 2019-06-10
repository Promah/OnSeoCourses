package com.example.day4norrisjokes

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel

    private var isInternetAvalible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val connectivityManager= this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        mainViewModel = ViewModelProviders.of(this, ModelFactory()).get(MainViewModel::class.java)

        mainViewModel.getLiveJokeText().observe(this, Observer {
            textViewJokeText.text = it.toString()
        })

        mainViewModel.getLiveJokeImgUrl().observe(this, Observer {
            it?.let {
                if (!it.isEmpty()) {
                    Picasso.get()
                        .load(it)
                        .fit()
                        .into(joke_photo)
                }
            }
        })

        mainViewModel.getLoadingState().observe(this, Observer {
            //            println("in View getLoadingState observe callback")
            it?.let {
                if (it)
                    progressBar.visibility = ProgressBar.VISIBLE
                else
                    progressBar.visibility = ProgressBar.GONE
            }
        })

        buttonNewJoke.setOnClickListener {
            isInternetAvalible = networkInfo!=null && networkInfo.isConnected

            if (!isInternetAvalible)
                textViewJokeText.text = "Error: no internet connection available!"
            else 
                mainViewModel.setNewJoke()
        }

    }

//    fun verifyAvailableNetwork(activity:AppCompatActivity):Boolean{
//        val connectivityManager= activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val networkInfo= connectivityManager.activeNetworkInfo
//        return  networkInfo!=null && networkInfo.isConnected
//    }
}
