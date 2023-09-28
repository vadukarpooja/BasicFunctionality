package com.example.apicalldemo.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apicalldemo.R
import com.example.apicalldemo.models.ColorsModel
import com.example.apicalldemo.models.ResponseItem


class ColorListAdapter(private val colorList: ArrayList<ColorsModel>, var onEditClick:(ResponseItem)->Unit ,var onDeleteClick:(ResponseItem)->Unit , var onItem:(ResponseItem)->Unit ) :
    RecyclerView.Adapter<ColorListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         val  name: TextView = view.findViewById(R.id.colorName)
         val edit:ImageView = view.findViewById<ImageView>(R.id.edit)
         val delete:ImageView = view.findViewById(R.id.delete)


        fun onBind(model: ColorsModel) {
            name.text = model.color
            //year.text = model.year


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorListAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.raw_color_list, parent, false)
        )
    }
    fun updateDataRefresh(viewModels:ArrayList<ColorsModel>) {
        colorList.clear()
        colorList.addAll(viewModels)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ColorListAdapter.ViewHolder, position: Int) {
        holder.onBind(colorList[position])
       // Log.e("test", "onBindViewHolder: "+colorList[position])
//        holder.itemView.setBackgroundColor(Color.parseColor(colorList[position].color))
        /*holder.edit.setOnClickListener {
            onEditClick.invoke(colorList[position])
        }
        holder.delete.setOnClickListener {
            onDeleteClick.invoke(colorList[position])
        }
        holder.itemView.setOnClickListener {
            onItem.invoke(colorList[position])
        }*/
    }

    override fun getItemCount(): Int {
        return colorList.size
    }
}