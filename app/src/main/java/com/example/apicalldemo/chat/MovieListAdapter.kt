package com.example.apicalldemo.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apicalldemo.R
import com.example.apicalldemo.models.Images

class MovieListAdapter(val context: Context, private val colorList: List<Images>) :
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView:ImageView = view.findViewById(R.id.img)


        fun onBind(model: Images) {
            Glide.with(context)
                .load(model)
                .into(imageView)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.raw_movie_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieListAdapter.ViewHolder, position: Int) {
        holder.onBind(colorList[position])
        holder.itemView.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return colorList.size
    }
}