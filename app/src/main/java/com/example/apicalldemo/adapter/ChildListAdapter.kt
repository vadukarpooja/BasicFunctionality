package com.example.apicalldemo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apicalldemo.R
import com.example.apicalldemo.models.IssuesModel


class ChildListAdapter(val list: ArrayList<IssuesModel>, var onClick:(IssuesModel) ->Unit) :
    RecyclerView.Adapter<ChildListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.raw_color_list, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(viewModels:ArrayList<IssuesModel>) {
        list.addAll(viewModels)
        notifyDataSetChanged()
    }





    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val obj = list[position]
        holder.name.text = position.toString()+"."+" "+obj.name
        holder.itemView.setOnClickListener {
            onClick.invoke(list[position])
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.colorName)
    }


}

