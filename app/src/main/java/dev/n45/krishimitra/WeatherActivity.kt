package dev.n45.krishimitra

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dev.n45.krishimitra.api.weather.WeatherData
import dev.n45.krishimitra.api.weather.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherActivity : AppCompatActivity() {
    private val apiKey: String = "906e5151b7b92bc612e877f7e4bba139"
    private lateinit var weatherService: WeatherService
    private lateinit var topAppBar: androidx.appcompat.widget.Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var mView: LinearLayout
    private var lat: Float = 12.9716f
    private var lon: Float = 77.5946f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        topAppBar = findViewById(R.id.topAppBar)

        topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        progressBar = findViewById(R.id.progressBar)
        mView = findViewById(R.id.mView)

        val actionRefresh = topAppBar.menu.findItem(R.id.action_refresh)

        weatherService = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)

        val sharedPreferences = getSharedPreferences("LocationData", MODE_PRIVATE)
        lat = sharedPreferences.getFloat("latitude", 0.0F)
        lon = sharedPreferences.getFloat("longitude", 0.0F)

        fetchWeather()

        actionRefresh.setOnMenuItemClickListener {
            fetchWeather()
            true
        }
    }

    private fun fetchWeather() {
        lifecycleScope.launch {
            val weatherData = weatherService.getWeather(apiKey, lat, lon)
            withContext(Dispatchers.Main) {
                updateUI(weatherData)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(weatherData: WeatherData) {
        topAppBar = findViewById(R.id.topAppBar)
        topAppBar.title = weatherData.name
        progressBar.visibility = ProgressBar.GONE
        mView.visibility = LinearLayout.VISIBLE
        findViewById<TextView>(R.id.temperature).text = "${weatherData.main.temp.toInt()}°C"
        findViewById<TextView>(R.id.currentState).text = weatherData.weather[0].main
        findViewById<TextView>(R.id.feelsLike).text = "${weatherData.main.feels_like}°C"
        findViewById<TextView>(R.id.humidity).text = "${weatherData.main.humidity}%"
        findViewById<TextView>(R.id.wind_speed).text = "${weatherData.wind.speed} m/s"
        val iconUrl = "https://rodrigokamada.github.io/openweathermap/images/${weatherData.weather[0].icon}_t@4x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(findViewById(R.id.weather_image))
    }
}