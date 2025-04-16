package dev.n45.krishimitra

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class ProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val topAppBar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBar)

        topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val productTitle = intent.getStringExtra("productName")
        val productPrice = intent.getStringExtra("productPrice")
        val productDescription = intent.getStringExtra("productDescription")
        val productImage = "wheat.png"

        val productPriceText = findViewById<TextView>(R.id.price)
        val productDescriptionText = findViewById<TextView>(R.id.ProductDescription)
        val productImageView = findViewById<ImageView>(R.id.product_image)

        val addButton = findViewById<Button>(R.id.addButton)

        topAppBar.title = productTitle
        productPriceText.text = productPrice
        productDescriptionText.text = productDescription

        Glide.with(this).load("http://100.100.1.2:8001/$productImage").into(productImageView)
    }
}