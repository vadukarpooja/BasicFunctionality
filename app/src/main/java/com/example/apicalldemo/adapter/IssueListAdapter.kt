package com.example.apicalldemo.adapter

import android.annotation.SuppressLint
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apicalldemo.R
import com.example.apicalldemo.models.IssuesModel
import com.google.android.gms.common.api.GoogleApi


class IssueListAdapter(val items: ArrayList<IssuesModel>) :
    RecyclerView.Adapter<IssueListAdapter.ViewHolder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.raw_issue_item, parent, false)
        v.setOnClickListener(this)
        return ViewHolder(v)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(viewModels:ArrayList< IssuesModel>) {
        items.addAll(viewModels)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataRefresh(viewModels:ArrayList< IssuesModel>) {
        items.clear()
        items.addAll(viewModels)
        notifyDataSetChanged()
    }

    fun addItem(position: Int, viewModel: IssuesModel) {
        items.add(position, viewModel)
        notifyItemInserted(position)
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = position.toString()+"."+" "+item.title

    }

    override fun getItemCount(): Int {
        return items.size
    }


     inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         val name: TextView = view.findViewById(R.id.title)
     }

     override fun onClick(v: View?) {

     }
 }