package dev.n45.krishimitra.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.n45.krishimitra.R
import dev.n45.krishimitra.api.data.Crop

class CropAdapter(private val crops: List<Crop>, private val listener: OnItemClickListener) : RecyclerView.Adapter<CropAdapter.CropViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(crop: Crop)
    }

    inner class CropViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val cropNameText: TextView = itemView.findViewById(R.id.cropName)
        private val cropTypeText: TextView = itemView.findViewById(R.id.cropType)
        private val cropDescriptionText: TextView = itemView.findViewById(R.id.cropDesc)
        private val cropImage: ImageView = itemView.findViewById(R.id.cropImage)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crop: Crop) {
            cropNameText.text = crop.title
            cropTypeText.text = crop.type
            cropDescriptionText.text = crop.description
            val image = crop.image

            Glide.with(itemView.context)
                .load("http://100.100.1.2:8001/$image")
                .into(cropImage)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(crops[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.crop_card, parent, false)
        return CropViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CropViewHolder, position: Int) {
        val currentCrop = crops[position]
        holder.bind(currentCrop)
    }

    override fun getItemCount() = crops.size
}