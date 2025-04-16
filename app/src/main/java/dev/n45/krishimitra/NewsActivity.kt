package dev.n45.krishimitra

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class NewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_news)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val topBar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBar)
        topBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val newsTitle = intent.getStringExtra("news_title")
        val newsImage = intent.getStringExtra("news_image")
        val newsDate = intent.getStringExtra("news_date")
        val newsContent = intent.getStringExtra("news_content")
        val newsTag = intent.getStringExtra("news_tag")

        val titleText = findViewById<android.widget.TextView>(R.id.news_title)
        val imageView = findViewById<com.google.android.material.imageview.ShapeableImageView>(R.id.news_image)
        val dateText = findViewById<android.widget.TextView>(R.id.news_date)
        val contentText = findViewById<android.widget.TextView>(R.id.news_desc)
        val tagText = findViewById<android.widget.TextView>(R.id.news_tag)

        titleText.text = newsTitle
        dateText.text = newsDate
        contentText.text = newsContent
        tagText.text = newsTag

        if (newsImage != null) {
            Glide.with(this)
                .load("http://100.100.1.2:8001/$newsImage")
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.f1)
        }
    }
}