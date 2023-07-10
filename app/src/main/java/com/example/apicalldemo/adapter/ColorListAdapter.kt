package com.example.apicalldemo.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.apicalldemo.R
import com.example.apicalldemo.models.ColorsModel
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


class ColorListAdapter(private val colorList: List<ColorsModel>) :
    RecyclerView.Adapter<ColorListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         val  name: TextView = view.findViewById(R.id.colorName)
         private val year:TextView = view.findViewById(R.id.year)


        fun onBind(model: ColorsModel) {
            name.text = model.name
            year.text = model.year
            name.setBackgroundColor(Color.parseColor(model.color))

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorListAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.raw_color_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ColorListAdapter.ViewHolder, position: Int) {
        holder.onBind(colorList[position])
        Log.e("test", "onBindViewHolder: "+colorList[position], )
    }

    override fun getItemCount(): Int {
        return colorList.size
    }
}