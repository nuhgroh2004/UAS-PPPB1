package com.example.uaspppb1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uaspppb1.Api.ApiService
import com.example.uaspppb1.Model.Mood
import com.example.uaspppb1.databinding.ActivityRiwayatBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiwayatBinding
    private lateinit var apiService: ApiService
    private lateinit var moodAdapter: MoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRiwayatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        apiService = (application as MyApp).apiService

        setupRecyclerView()
        fetchAllMoods()
    }

    private fun setupRecyclerView() {
        moodAdapter = MoodAdapter(emptyList(), onEditClick = { mood ->
            // Handle edit click
        }, onDeleteClick = { mood ->
            // Handle delete click
        })
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RiwayatActivity)
            adapter = moodAdapter
        }
    }

    private fun fetchAllMoods() {
        binding.progressBar.visibility = View.VISIBLE
        apiService.getAllData("19B5K", "mood").enqueue(object : Callback<List<Mood>> {
            override fun onResponse(call: Call<List<Mood>>, response: Response<List<Mood>>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val moodList = response.body()
                    if (!moodList.isNullOrEmpty()) {
                        moodAdapter = MoodAdapter(moodList, onEditClick = { mood ->
                            // Handle edit click
                        }, onDeleteClick = { mood ->
                            // Handle delete click
                        })
                        binding.recyclerView.adapter = moodAdapter
                    } else {
                        Toast.makeText(this@RiwayatActivity, "No data available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("RiwayatActivity", "Failed to fetch moods: ${response.errorBody()?.string()}")
                    Toast.makeText(this@RiwayatActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Mood>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e("RiwayatActivity", "Error: ${t.message}")
                Toast.makeText(this@RiwayatActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}