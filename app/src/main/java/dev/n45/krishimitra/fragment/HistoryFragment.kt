package dev.n45.krishimitra.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.n45.krishimitra.R
import dev.n45.krishimitra.adapter.HistoryAdapter
import dev.n45.krishimitra.api.Client
import dev.n45.krishimitra.api.data.HistoryItem
import kotlinx.coroutines.launch

private lateinit var historyAdapter: HistoryAdapter
private lateinit var recyclerView: RecyclerView

class HistoryFragment : Fragment(), HistoryAdapter.OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchHistory()
    }

    private fun fetchHistory() {
        lifecycleScope.launch {
            try {
                val sharedPreferences = requireActivity().getSharedPreferences("UserData", AppCompatActivity.MODE_PRIVATE)
                val token = sharedPreferences.getString("access_token", null)
                val history = Client.apiService.getHistory("Bearer $token")
                historyAdapter = HistoryAdapter(history, this@HistoryFragment)
                recyclerView.adapter = historyAdapter
                Log.d("HistoryFragment", "Fetched history: $history")
            } catch (e: Exception) {
                Log.e("HistoryFragment", "Error fetching history: ${e.message}")
            }
        }
    }

    override fun onItemClick(historyItem: HistoryItem) {
        // Handle item click
        Log.d("HistoryFragment", "Clicked item: ${historyItem.productId}")
    }
}