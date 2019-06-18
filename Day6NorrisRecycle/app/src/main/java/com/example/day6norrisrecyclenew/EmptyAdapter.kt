package com.example.day6norrisrecyclenew


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class EmptyAdapter(val con : Context) : RecyclerView.Adapter<EmptyHolder>() {
    override fun onBindViewHolder(p0: EmptyHolder, p1: Int) {
    }

    override fun getItemCount(): Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmptyHolder = EmptyHolder(LayoutInflater.from(con).inflate(R.layout.jokes_list_item, parent, false))

}
class EmptyHolder(v : View) : RecyclerView.ViewHolder(v){

}