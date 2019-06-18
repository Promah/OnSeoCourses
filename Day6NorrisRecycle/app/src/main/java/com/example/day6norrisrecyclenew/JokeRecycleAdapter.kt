package com.example.day6norrisrecyclenew

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.jokes_list_item.view.*
import kotlinx.android.synthetic.main.jokes_list_item_btn.view.*
import java.lang.Exception

class JokeRecycleAdapter private constructor( val context : Context, val dataFromBackEnd : BackEnd) : RecyclerView.Adapter<JokeHolder>() {

    private var jokesCountInStackPlusOne = BackEnd.JOKES_STACK_SIZE + 1
    private var jokeListSize = jokesCountInStackPlusOne

    private var  arrJokes = mutableListOf<String>()
    private var  arrJokesImgURI = mutableListOf<String>()

    companion object {
        const val VIEW_TYPE_JOKE = 1
        const val VIEW_TYPE_BUTTON = 2
        private var instance : JokeRecycleAdapter? = null
        @Synchronized fun getInstance(contextI: Context, dataFromBackEndI: BackEnd): JokeRecycleAdapter {
            if (instance == null)  // hope is fun is Synchronized it naw thread safe
                instance = JokeRecycleAdapter(contextI, dataFromBackEndI)
            return instance!!
        }
    }

    init {
        println("in JokeRecycleAdapter init")

        dataFromBackEnd.getJokeTexts().observeForever {
            it?.forEach {
                if (!it.isEmpty())
                    arrJokes.add(it)
            }
//            notifyDataSetChanged()
//            notifyItemRangeChanged(jokeListSize-BackEnd.JOKES_STACK_SIZE, BackEnd.JOKES_STACK_SIZE)
//            notifyItemRemoved(jokeListSize-BackEnd.JOKES_STACK_SIZE)
            autoNotify()
        }
        dataFromBackEnd.getJokesImgUrl().observeForever {
            it?.forEach {
                if (!it.isEmpty())
                    arrJokesImgURI.add(it)
            }
//            notifyDataSetChanged()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeHolder =
         when(viewType){
            VIEW_TYPE_JOKE -> JokeHolderContent(LayoutInflater.from(context).inflate(R.layout.jokes_list_item, parent, false))
            else -> JokeHolderBtn(LayoutInflater.from(context).inflate(R.layout.jokes_list_item_btn, parent, false))
        }

    override fun getItemCount(): Int = jokeListSize
//    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onBindViewHolder(holder: JokeHolder, position: Int) {
        try {
//            holder.textHolder?.text = "$position"
            holder.textHolder?.text = arrJokes[position]
            holder.photoHolder?.let {
                val imgSrc = arrJokesImgURI[position]
                println("imgSrc = $imgSrc")
                Picasso.get()
                    .load(imgSrc)
                    .fit()
                    .into(it)
            }
            holder.btn?.setOnClickListener {
                jokeListSize += BackEnd.JOKES_STACK_SIZE
                dataFromBackEnd.getNewJokeTextsStack()
//                Toast.makeText(context, "position = $position", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Why it scrolling to top?", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (position == jokeListSize-1)
            VIEW_TYPE_BUTTON
        else
            VIEW_TYPE_JOKE

    fun RecyclerView.Adapter<*>.autoNotify() {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return true
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                println("jokeListSize-BackEnd.JOKES_STACK_SIZE = ${jokeListSize-BackEnd.JOKES_STACK_SIZE}")
                println("oldItemPosition = $oldItemPosition")
                println("newItemPosition = $newItemPosition")
                return if (newItemPosition < jokeListSize-BackEnd.JOKES_STACK_SIZE) false else true
            }

            override fun getOldListSize() = jokeListSize-BackEnd.JOKES_STACK_SIZE

            override fun getNewListSize() = jokeListSize
        })

        diff.dispatchUpdatesTo(this)
    }

}

abstract class JokeHolder(v : View) : RecyclerView.ViewHolder(v){
    open val textHolder : TextView? = null
    open val photoHolder : ImageView? = null
    open val btn: Button? = null
}

class JokeHolderContent(v : View) : JokeHolder(v){
    override val textHolder = v.jokeText
    override val photoHolder = v.jokePhoto
}

class JokeHolderBtn(v : View) : JokeHolder(v){
    override val btn = v.buttonNewJokes
}
