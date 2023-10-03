package com.example.apicalldemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apicalldemo.R
import com.example.apicalldemo.models.IssuesModel

class MatchOperationAdapter(val list: ArrayList<IssuesModel>,val onClick:(IssuesModel)->Unit) :
    RecyclerView.Adapter<MatchOperationAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img = view.findViewById<ImageView>(R.id.img)
        val num = view.findViewById<TextView>(R.id.number)
        val num1 = view.findViewById<TextView>(R.id.number1)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MatchOperationAdapter.ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.raw_math_operaion, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: MatchOperationAdapter.ViewHolder, position: Int) {
        val obj = list[position]
        holder.num.text = obj.number.toString()
        holder.itemView.setOnClickListener {
            onClick.invoke(list[position])

            holder.num.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}