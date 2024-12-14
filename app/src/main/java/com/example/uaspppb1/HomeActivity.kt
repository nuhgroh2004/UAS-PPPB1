package com.example.uaspppb1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.uaspppb1.Api.ApiService
import com.example.uaspppb1.Model.Mood
import com.example.uaspppb1.databinding.ActivityHomeBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = (application as MyApp).apiService

        setupClickAnimation(binding.imageSedih, "Sedih")
        setupClickAnimation(binding.imageMarah, "Marah")
        setupClickAnimation(binding.imageBahagia, "Bahagia")

        fetchLatestMood()

        binding.profilSection.setOnClickListener {
            val intent = Intent(this, ProfilActivity::class.java)
            startActivity(intent)
        }
        binding.btnRiwayat.setOnClickListener {
            val intent = Intent(this, RiwayatActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupClickAnimation(imageView: ImageView, mood: String) {
        imageView.setOnClickListener {
            val scaleUp = ScaleAnimation(
                1f, 1.2f,
                1f, 1.2f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 100
                fillAfter = true
            }

            val scaleDown = ScaleAnimation(
                1.2f, 1f,
                1.2f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                startOffset = 100
                duration = 100
                fillAfter = true
            }

            imageView.startAnimation(scaleUp)
            imageView.postDelayed({ imageView.startAnimation(scaleDown) }, 100)

            val indonesianLocale = Locale("id", "ID")
            val dateFormatSymbols = DateFormatSymbols(indonesianLocale).apply {
                weekdays = arrayOf("Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")
                shortMonths = arrayOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")
            }
            val dateFormat = SimpleDateFormat("EEE, MMM d\nh:mm a", indonesianLocale)
            dateFormat.dateFormatSymbols = dateFormatSymbols
            val timestamp = dateFormat.format(Date())
            val newMood = Mood(mood = mood, timestamp = timestamp, id_user = "")
            Log.d("HomeActivity", "Posting mood: $newMood")
            postMood(newMood)
        }
    }

    private fun postMood(mood: Mood) {
        lifecycleScope.launch {
            val userDao = (application as MyApp).getDatabase().userDao()
            val user = userDao.getUser()
            val idUser = user?.id_user ?: return@launch

            val moodWithIdUser = Mood(mood = mood.mood, timestamp = mood.timestamp, id_user = idUser)
            apiService.postData("19B5K", "mood", moodWithIdUser).enqueue(object : Callback<Mood> {
                override fun onResponse(call: Call<Mood>, response: Response<Mood>) {
                    if (response.isSuccessful) {
                        Log.d("HomeActivity", "Mood posted successfully: ${response.body()}")
                        fetchLatestMood()
                    } else {
                        Log.e("HomeActivity", "Failed to post mood: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<Mood>, t: Throwable) {
                    Log.e("HomeActivity", "Error: ${t.message}")
                }
            })
        }
    }

   private fun fetchLatestMood() {
        lifecycleScope.launch {
            val userDao = (application as MyApp).getDatabase().userDao()
            val user = userDao.getUser()
            val idUser = user?.id_user ?: return@launch

            apiService.getDataByUserId("19B5K", "mood", idUser).enqueue(object : Callback<List<Mood>> {
                override fun onResponse(call: Call<List<Mood>>, response: Response<List<Mood>>) {
                    if (response.isSuccessful) {
                        val moodList = response.body()
                        val filteredMoodList = moodList?.filter { it.id_user == idUser }
                        if (!filteredMoodList.isNullOrEmpty()) {
                            val latestMood = filteredMoodList.last()
                            displayMood(latestMood)
                        } else {
                            Log.e("HomeActivity", "No moods found for the user")
                        }
                    } else {
                        Log.e("HomeActivity", "Failed to fetch moods: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<List<Mood>>, t: Throwable) {
                    Log.e("HomeActivity", "Error: ${t.message}")
                }
            })
        }
    }

    private fun displayMood(mood: Mood) {
        val textTime: TextView = findViewById(R.id.text_time)
        val textFeeling: TextView = findViewById(R.id.text_feeling)
        val imagePlaceholder: LinearLayout = findViewById(R.id.img_placeholder)

        textTime.text = mood.timestamp
        textFeeling.text = mood.mood

        val moodImageRes = when (mood.mood) {
            "Sedih" -> R.drawable.sedih
            "Marah" -> R.drawable.marah
            "Bahagia" -> R.drawable.bahagia
            else -> R.drawable.circle_placeholder
        }
        imagePlaceholder.setBackgroundResource(moodImageRes)
    }
}