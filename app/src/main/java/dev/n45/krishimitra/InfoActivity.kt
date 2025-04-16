package dev.n45.krishimitra

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.net.toUri

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val topAppBar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBar)

        topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val githubBtn = findViewById<Button>(R.id.githubBtn)
        val twitterBtn = findViewById<Button>(R.id.twitterBtn)

        githubBtn.setOnClickListener {
            val url = "https://github.com/n45dev"
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
            intent.data = url.toUri()
            startActivity(intent)
        }

        twitterBtn.setOnClickListener {
            val url = "https://twitter.com/n45dev"
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
            intent.data = url.toUri()
            startActivity(intent)
        }
    }
}