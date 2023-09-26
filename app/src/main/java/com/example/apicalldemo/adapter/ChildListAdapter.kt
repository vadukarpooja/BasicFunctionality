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


class ChildListAdapter(val list: MutableList<Category>,var childList:ArrayList<IssuesModel>, var onClick:(Int) ->Unit) :
    RecyclerView.Adapter<ChildListAdapter.ViewHolder>(){
    private var expandedPosition = -1
    var position1 = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.raw_issue_item, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(viewModels:ArrayList<Category>) {
        list.addAll(viewModels)
        notifyDataSetChanged()
    }





    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val obj = list[position]
        position1 = position
        holder.name.text = position.toString()+"."+" "+obj.name
        holder.itemView.setOnClickListener {
            onClick.invoke(position)
        }
        val adapter = TabSyncAdapter(obj.listOfItems,onClick= {
        })
        holder.list.adapter = adapter
        holder.itemView.setOnClickListener {
            onClick.invoke(position)
            if (position == expandedPosition) {
                notifyItemChanged(position)
                expandedPosition = -1
            } else {
                if (expandedPosition != -1) {
                    notifyItemChanged(expandedPosition)
                }
                expandedPosition = position
                notifyItemChanged(position)
            }
        }


        if (position== expandedPosition) {
            holder.list.visibility = View.VISIBLE
        } else {
            holder.list.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.title)
        val list:RecyclerView = view.findViewById(R.id.recycleChildIssueList)
    }
}

