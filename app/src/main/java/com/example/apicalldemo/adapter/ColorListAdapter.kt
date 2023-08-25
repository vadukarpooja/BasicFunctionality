package com.example.apicalldemo.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apicalldemo.R
import com.example.apicalldemo.models.ColorsModel


class ColorListAdapter(private val colorList: List<ColorsModel>,var onClick:(List<ColorsModel>)->Unit ) :
    RecyclerView.Adapter<ColorListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         val  name: TextView = view.findViewById(R.id.colorName)
         val year:TextView = view.findViewById(R.id.year)


        fun onBind(model: ColorsModel) {
            name.text = model.name
            year.text = model.year


        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorListAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.raw_color_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ColorListAdapter.ViewHolder, position: Int) {
        holder.onBind(colorList[position])
        Log.e("test", "onBindViewHolder: "+colorList[position])
        holder.itemView.setBackgroundColor(Color.parseColor(colorList[position].color))
        holder.itemView.setOnClickListener {
            onClick.invoke(colorList)
        }
    }

    override fun getItemCount(): Int {
        return colorList.size
    }
}