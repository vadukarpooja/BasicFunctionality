package com.example.apicalldemo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apicalldemo.R
import com.example.apicalldemo.models.Category
import com.example.apicalldemo.models.IssuesModel
import com.example.apicalldemo.models.Item

class TabSyncAdapter(val list: List<Item>, var onClick: (Item) -> Unit) :
    RecyclerView.Adapter<TabSyncAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.raw_item, parent, false)
        return ViewHolder(v)
    }



    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val obj = list[position]
        holder.name.text = position.toString() + "." + " " + obj.content
        holder.itemView.setOnClickListener {
            onClick.invoke(list[position])
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.title)
    }


}

