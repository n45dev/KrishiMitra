package dev.n45.krishimitra.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.n45.krishimitra.R
import dev.n45.krishimitra.api.data.HistoryItem

class HistoryAdapter(private val history: List<HistoryItem>, private val listener: OnItemClickListener) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(historyItem: HistoryItem)
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val productNameText: TextView = itemView.findViewById(R.id.product_name)
        private val mTypeText: TextView = itemView.findViewById(R.id.mType)
        private val dateText: TextView = itemView.findViewById(R.id.date_text)
        private val quantityText: TextView = itemView.findViewById(R.id.quantity)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(historyItem: HistoryItem) {
            productNameText.text = historyItem.productId
            mTypeText.text = historyItem.mType
            dateText.text = historyItem.date
            quantityText.text = buildString { append("Quantity: ").append(historyItem.quantity) }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(history[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_item_card, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentHistoryItem = history[position]
        holder.bind(currentHistoryItem)
    }

    override fun getItemCount() = history.size
}