package dev.n45.krishimitra.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.n45.krishimitra.NewsActivity
import dev.n45.krishimitra.adapter.NewsAdapter
import dev.n45.krishimitra.R
import dev.n45.krishimitra.WeatherActivity
import dev.n45.krishimitra.api.Client
import dev.n45.krishimitra.api.data.News
import dev.n45.krishimitra.api.weather.WeatherData
import dev.n45.krishimitra.api.weather.WeatherService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

private const val apiKey: String = "906e5151b7b92bc612e877f7e4bba139"
private var lat: Float = 0.0f
private var lon: Float = 0.0f
private lateinit var weatherService: WeatherService
private lateinit var newsAdapter: NewsAdapter
private lateinit var recyclerView: RecyclerView

class HomeFragment : Fragment(), NewsAdapter.OnItemClickListener {
    private lateinit var cityText: TextView
    private lateinit var temperatureText: TextView
    private lateinit var weatherImage: ImageView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weatherCard = view.findViewById<CardView>(R.id.weather_card)
        cityText = weatherCard.findViewById(R.id.city_name)
        temperatureText = weatherCard.findViewById(R.id.temp)
        weatherImage = weatherCard.findViewById(R.id.weather_icon)
        progressBar = weatherCard.findViewById(R.id.progressBar)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        weatherCard?.setOnClickListener {
            val intent = Intent(context, WeatherActivity::class.java)
            startActivity(intent)
        }

        val sharedPreferences = requireContext().getSharedPreferences("LocationData", AppCompatActivity.MODE_PRIVATE)
        lat = sharedPreferences.getFloat("latitude", 0.0F)
        lon = sharedPreferences.getFloat("longitude", 0.0F)

        weatherService = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)

        fetchWeather()

        fetchNews()
    }

    private fun fetchWeather() {
        lifecycleScope.launch {
            try {
                val weatherData = weatherService.getWeather(apiKey, lat, lon)
                updateWeatherUI(weatherData)
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error fetching weather: ${e.message}")
                val toast = Toast.makeText(requireContext(), "Error fetching weather data", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    private fun fetchNews() {
        lifecycleScope.launch {
            try {
                val sharedPreferences = requireActivity().getSharedPreferences("UserData", AppCompatActivity.MODE_PRIVATE)
                val token = sharedPreferences.getString("access_token", null)
                val news = Client.apiService.getNews("Bearer $token")
                newsAdapter = NewsAdapter(news, this@HomeFragment)
                recyclerView.adapter = newsAdapter
            } catch (e: Exception) {
                Log.e("BuyFragment", "Error fetching news: ${e.message}")
                val toast = Toast.makeText(requireContext(), "Error fetching news", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    private fun updateWeatherUI(weatherData: WeatherData) {
        progressBar.visibility = ProgressBar.GONE
        cityText.text = weatherData.name
        temperatureText.text = "${weatherData.main.temp.toInt()}°C"
        val iconUrl = "https://rodrigokamada.github.io/openweathermap/images/${weatherData.weather[0].icon}_t@4x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(weatherImage)
        weatherImage.visibility = ImageView.VISIBLE
    }

    override fun onItemClick(news: News) {
        val intent = Intent(context, NewsActivity::class.java)
        intent.putExtra("news_title", news.title)
        intent.putExtra("news_image", news.image)
        intent.putExtra("news_date", news.date)
        intent.putExtra("news_short_description", news.short_description)
        intent.putExtra("news_content", news.content)
        intent.putExtra("news_tag", news.tag)
        startActivity(intent)
        Log.d("BuyFragment", "Clicked on news: ${news.id}")
    }
}