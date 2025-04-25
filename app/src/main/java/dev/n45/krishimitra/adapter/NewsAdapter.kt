package dev.n45.krishimitra.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.n45.krishimitra.R
import dev.n45.krishimitra.api.data.News
import dev.n45.krishimitra.fragment.HomeFragment

class NewsAdapter(private val newsList: List<News>, private val listener: HomeFragment) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(news: News)
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val newsTitleText: TextView = itemView.findViewById(R.id.news_title)
        private val newsDateText: TextView = itemView.findViewById(R.id.news_date)
        private val newsShortDescriptionText: TextView = itemView.findViewById(R.id.news_desc)
        private val newsImage: ImageView = itemView.findViewById(R.id.news_image)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(news: News) {
            newsTitleText.text = news.title
            newsDateText.text = news.date
            newsShortDescriptionText.text = news.short_description

            val image = news.image

            if (image != null) {
                Glide.with(itemView.context)
                    .load("http://10.0.2.2:8001/$image")
                    .into(newsImage)
            } else {
                newsImage.setImageResource(R.drawable.f1)
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(newsList[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.news_card, parent, false)
        return NewsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentNews = newsList[position]
        holder.bind(currentNews)
    }

    override fun getItemCount() = newsList.size
}