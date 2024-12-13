package com.example.uaspppb1

import android.content.Intent
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
            val intent = Intent(this, EditMoodActivity::class.java)
            intent.putExtra("MOOD_ID", mood.id)
            startActivity(intent)
        }, onDeleteClick = { moodId ->
            deleteMood(moodId)
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
                        val sortedMoodList = moodList.sortedByDescending { it.timestamp }
                        moodAdapter.updateData(sortedMoodList)
                    } else {
                        Toast.makeText(this@RiwayatActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("RiwayatActivity", "Gagal: ${response.errorBody()?.string()}")
                    Toast.makeText(this@RiwayatActivity, "Gagal", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Mood>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e("RiwayatActivity", "Error: ${t.message}")
                Toast.makeText(this@RiwayatActivity, "Jaringan error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteMood(moodId: String) {
        apiService.deleteMood("19B5K", "mood", moodId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RiwayatActivity, "Berhasil menghapus", Toast.LENGTH_SHORT).show()
                    fetchAllMoods()
                } else {
                    Log.e("RiwayatActivity", "Gagal: ${response.errorBody()?.string()}")
                    Toast.makeText(this@RiwayatActivity, "Gagal", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("RiwayatActivity", "Error: ${t.message}")
                Toast.makeText(this@RiwayatActivity, "Jaringan error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onResume() {
        super.onResume()
        fetchAllMoods()
    }
}