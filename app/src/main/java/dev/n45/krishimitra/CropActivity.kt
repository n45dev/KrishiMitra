package dev.n45.krishimitra

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class CropActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crop)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val topAppBar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBar)

        topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val cropTitle = intent.getStringExtra("crop_title")
        val cropImage = intent.getStringExtra("crop_image")

        val cropImageView = findViewById<com.google.android.material.imageview.ShapeableImageView>(R.id.cropImage)
        val cropDescription = findViewById<TextView>(R.id.cropDesc)
        val cropPhRange = findViewById<TextView>(R.id.phRange)
        val cropType = findViewById<TextView>(R.id.cropType)
        val cropSoilType = findViewById<TextView>(R.id.soilType)
        val cropTempRange = findViewById<TextView>(R.id.tempRange)
        val cropRainfallRequired = findViewById<TextView>(R.id.rainfallRequired)
        val cropMarketValue = findViewById<TextView>(R.id.marketValue)

        topAppBar.title = cropTitle
        cropDescription.text = intent.getStringExtra("crop_desc")
        cropPhRange.text = intent.getStringExtra("crop_ph_range")
        cropType.text = intent.getStringExtra("crop_type")
        cropSoilType.text = intent.getStringExtra("crop_soil_type")
        cropTempRange.text = intent.getStringExtra("crop_temp_range")
        cropRainfallRequired.text = intent.getStringExtra("crop_rainfall_required")
        cropMarketValue.text = intent.getStringExtra("crop_market_value")

        Glide.with(this)
            .load("http://100.100.1.2:8001/${cropImage}")
            .into(cropImageView)
    }
}