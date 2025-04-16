package dev.n45.krishimitra.adapter

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.n45.krishimitra.R
import dev.n45.krishimitra.api.data.Message

class ChatAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewMessage: TextView = itemView.findViewById(R.id.textViewMessage)
        val timeText: TextView = itemView.findViewById(R.id.timeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.textViewMessage.text = message.text
        holder.timeText.text = message.time

        val layoutParams = holder.textViewMessage.layoutParams as LinearLayout.LayoutParams
        val lp = holder.timeText.layoutParams as LinearLayout.LayoutParams
        if (message.isSentByUser) {
            layoutParams.gravity = Gravity.END
            lp.gravity = Gravity.END
            holder.textViewMessage.setBackgroundResource(R.drawable.user_message_background)
            holder.textViewMessage.setTextColor(Color.WHITE)
        } else {
            layoutParams.gravity = Gravity.START
            lp.gravity = Gravity.START
            holder.textViewMessage.setBackgroundResource(R.drawable.other_message_background)
            holder.textViewMessage.setTextColor(Color.BLACK)
        }
        holder.textViewMessage.layoutParams = layoutParams
    }

    override fun getItemCount(): Int = messages.size
}