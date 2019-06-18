package com.example.day6norrisrecyclenew

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var dataFromBackEnd : BackEnd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataFromBackEnd = ViewModelProviders.of(this).get(BackEnd::class.java)

        dataFromBackEnd.getLoadingState().observe(this, Observer {
            it?.let { isDataLoading ->
                if (isDataLoading)
                    progressBar.visibility = ProgressBar.VISIBLE
                else
                    progressBar.visibility = ProgressBar.GONE
            }
        })

        recyclerViewJokesList.layoutManager = LinearLayoutManager(this)

        dataFromBackEnd.getFirstJokesTexts()
        recyclerViewJokesList.adapter = EmptyAdapter(this)// it may be loading screen, but now it empty

        //when first jokes data ready -> init JokeRecycleAdapter
        dataFromBackEnd.getJokeTexts().observe(this, Observer {
            recyclerViewJokesList.adapter = JokeRecycleAdapter.getInstance(this, dataFromBackEnd)
        })

    }
}
