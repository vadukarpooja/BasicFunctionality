package com.example.apicalldemo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apicalldemo.R
import com.example.apicalldemo.models.IssuesModel


class IssueListAdapter(val items: ArrayList<IssuesModel>,var onClick:(IssuesModel) ->Unit) :
    RecyclerView.Adapter<IssueListAdapter.ViewHolder>(){
     var expandedPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.raw_color_list, parent, false)
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
       /* val adapter = ChildListAdapter(list(), arrayListOf(),onClick= {
        })
        holder.list.adapter = adapter*/
        holder.itemView.setOnClickListener {
            onClick.invoke(items[position])
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
        return items.size
    }


     inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         val name: TextView = view.findViewById(R.id.colorName)
         val list:RecyclerView = view.findViewById(R.id.recycleIssueList)
     }
    fun list(): ArrayList<IssuesModel> {
        val list = ArrayList<IssuesModel>()
        list.add(IssuesModel(name = "Test1"))
        list.add(IssuesModel(name = "Test2"))
        list.add(IssuesModel(name = "Test3"))
        return list
    }

 }