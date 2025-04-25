package dev.n45.krishimitra

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.n45.krishimitra.fragment.CropsFragment
import dev.n45.krishimitra.fragment.HomeFragment
import dev.n45.krishimitra.fragment.MarketFragment
import dev.n45.krishimitra.fragment.PodcastsFragment
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var homeFragment: HomeFragment
    private lateinit var cropsFragment: CropsFragment
    private lateinit var podcastsFragment: PodcastsFragment
    private lateinit var marketFragment: MarketFragment
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)

        if (token == null) {
            logOut()
        }

        homeFragment = HomeFragment()
        cropsFragment = CropsFragment()
        podcastsFragment = PodcastsFragment()
        marketFragment = MarketFragment()

        bottomNavigationView = findViewById(R.id.btmNavBar)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, homeFragment, "HOME")
        transaction.add(R.id.container, cropsFragment, "CROPS").hide(cropsFragment)
        transaction.add(R.id.container, marketFragment, "MARKET").hide(marketFragment)
        transaction.commit()

        loadFragment(homeFragment)

        bottomNavigationView.setOnItemSelectedListener {
            try {
                when (it.itemId) {
                    R.id.home -> {
                        loadFragment(homeFragment)
                        true
                    }
                    R.id.crops -> {
                        loadFragment(cropsFragment)
                        true
                    }
                    R.id.market -> {
                        loadFragment(marketFragment)
                        true
                    }
                    else -> false
                }
            } catch (e: Exception) {
                throw e
            }
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    val sharedPreferences = getSharedPreferences("LocationData", MODE_PRIVATE)
                    sharedPreferences.edit {
                        putFloat("latitude", lat.toFloat())
                        putFloat("longitude", lon.toFloat())
                    }
                    println("Lat: $lat, Long: $lon")
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        }
    }

    private fun logOut() {
        val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        sharedPreferences.edit {
            remove("access_token")
        }
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()

        currentFragment?.let {
            transaction.hide(it)
        }

        transaction.show(fragment)
        transaction.commit()

        currentFragment = fragment
    }
}