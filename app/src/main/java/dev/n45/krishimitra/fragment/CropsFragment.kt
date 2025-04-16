package dev.n45.krishimitra.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import dev.n45.krishimitra.CropActivity
import dev.n45.krishimitra.R
import dev.n45.krishimitra.adapter.CropAdapter
import dev.n45.krishimitra.api.Client
import dev.n45.krishimitra.api.data.Crop
import kotlinx.coroutines.launch
import android.Manifest
import android.content.pm.PackageManager
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.lifecycleScope

private lateinit var cropAdapter: CropAdapter
private lateinit var recyclerView: RecyclerView
private lateinit var recyclerViewResults: RecyclerView
private var crops: List<Crop> = emptyList()

class CropsFragment : Fragment(), CropAdapter.OnItemClickListener {

    private val voiceInputLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!results.isNullOrEmpty()) {
                val spokenText = results[0]
                // Open SearchView, set text, and trigger search
                val searchView = requireView().findViewById<SearchView>(R.id.search_view)
                searchView.show()
                searchView.editText.setText(spokenText)
                searchCrops(spokenText)
            }
        }
    }

    // Launcher for microphone permission
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            startVoiceInput()
        } else {
            Toast.makeText(requireContext(), "Microphone permission required for voice search", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crops_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerViewResults = view.findViewById(R.id.recyclerViewResults)
        recyclerViewResults.layoutManager = LinearLayoutManager(requireContext())

        fetchCrops()

        val searchBar = view.findViewById<SearchBar>(R.id.search_bar)
        val searchView = view.findViewById<SearchView>(R.id.search_view)

        // Menu item handling for search and voice
        searchBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_vioce -> {
                    checkPermissionAndStartVoiceInput()
                    true
                }
                R.id.action_refresh -> {
                    fetchCrops()
                    true
                }
                else -> false
            }
        }

        setupSearch(searchBar, searchView)
    }

    private fun fetchCrops() {
        try {
            lifecycleScope.launch {
                try {
                    val sharedPreferences = requireActivity().getSharedPreferences("UserData", AppCompatActivity.MODE_PRIVATE)
                    val token = sharedPreferences.getString("access_token", null)
                    crops = Client.apiService.getCrops("Bearer $token")
                    cropAdapter = CropAdapter(crops, this@CropsFragment)
                    recyclerView.adapter = cropAdapter
                    recyclerView.visibility = View.VISIBLE
                    recyclerViewResults.visibility = View.GONE
                } catch (e: Exception) {
                    Log.e("CropsFragment", "Error fetching crops: ${e.message}")
                }
            }
        } catch (e: Exception) {
            Log.e("CropsFragment", "Error in fetchCrops: ${e.message}")
            Toast.makeText(requireContext(), "Error fetching crops", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSearch(searchBar: SearchBar, searchView: SearchView) {
        try {
            searchBar.setOnClickListener {
                searchView.show()
            }

            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val query = searchView.text.toString().trim()
                searchCrops(query)
                searchView.hide()
                false
            }

            searchView.addTransitionListener { _, _, newState ->
                when (newState) {
                    SearchView.TransitionState.SHOWING -> {
                        recyclerView.visibility = View.GONE
                        recyclerViewResults.visibility = View.VISIBLE
                    }
                    SearchView.TransitionState.HIDDEN -> {
                        recyclerView.visibility = View.VISIBLE
                        recyclerViewResults.visibility = View.GONE
                        cropAdapter = CropAdapter(crops, this@CropsFragment)
                        recyclerView.adapter = cropAdapter
                    }
                    else -> {}
                }
            }

            // Live search
            searchView.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    searchCrops(s.toString().trim())
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        } catch (e: Exception) {
            Log.e("CropsFragment", "Error setting up search: ${e.message}")
        }
    }

    private fun searchCrops(title: String) {
        try {
            val filteredCropList = if (title.isEmpty()) {
                crops
            } else {
                crops.filter { crop ->
                    crop.title.lowercase().contains(title.lowercase())
                }
            }
            cropAdapter = CropAdapter(filteredCropList, this@CropsFragment)
            recyclerViewResults.adapter = cropAdapter
        } catch (e: Exception) {
            Log.e("CropsFragment", "Error filtering crops: ${e.message}")
        }
    }

    private fun checkPermissionAndStartVoiceInput() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                startVoiceInput()
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN") // Adjust for local languages
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak crop name...")
        }
        try {
            voiceInputLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Voice typing not available", Toast.LENGTH_SHORT).show()
            Log.e("CropsFragment", "Voice input error: ${e.message}")
        }
    }

    override fun onItemClick(crop: Crop) {
        val intent = Intent(context, CropActivity::class.java)
        intent.putExtra("crop_id", crop.id)
        intent.putExtra("crop_title", crop.title)
        intent.putExtra("crop_image", crop.image)
        intent.putExtra("crop_description", crop.description)
        intent.putExtra("crop_duration", crop.duration)
        intent.putExtra("crop_type", crop.type)
        intent.putExtra("crop_soil_type", crop.soilType)
        intent.putExtra("crop_temp_range", crop.tempRange)
        intent.putExtra("crop_is_rainfall_required", crop.rainfallRequired)
        intent.putExtra("crop_ph_range", crop.phRange)
        intent.putExtra("crop_market_value", crop.marketValue)
        startActivity(intent)
        Log.d("CropsFragment", "Clicked on crop: ${crop.id}")
    }
}