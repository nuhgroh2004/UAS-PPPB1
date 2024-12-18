package com.example.uaspppb1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.uaspppb1.Api.ApiService
import com.example.uaspppb1.Model.Mood
import com.example.uaspppb1.databinding.ActivityEditMoodBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditMoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditMoodBinding
    private lateinit var apiService: ApiService
    private var selectedMoodString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditMoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = (application as MyApp).apiService

        val moodId = intent.getStringExtra("MOOD_ID")
        if (moodId != null) {
            fetchMoodDetails(moodId)
        } else {
            Toast.makeText(this, "Mood tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.backButton.setOnClickListener {
            val intent = Intent(this, RiwayatActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.buttonSimpan.setOnClickListener {
            selectedMoodString?.let { mood ->
                updateMood(moodId ?: "", mood)
            }
        }

        setupMoodSelection()
    }

    private fun fetchMoodDetails(moodId: String) {
        lifecycleScope.launch {
            val userDao = (application as MyApp).getDatabase().userDao()
            val user = userDao.getUser()
            val idUser = user?.id_user ?: return@launch

            apiService.getDataByUserId("19B5K", "mood", idUser).enqueue(object : Callback<List<Mood>> {
                override fun onResponse(call: Call<List<Mood>>, response: Response<List<Mood>>) {
                    if (response.isSuccessful) {
                        val moodList = response.body()
                        val mood = moodList?.find { it.id == moodId }
                        if (mood != null) {

                        } else {
                            Log.e("EditMoodActivity", "Mood tidak ditemukan")
                            Toast.makeText(this@EditMoodActivity, "Mood tidak ditemukan", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    } else {
                        Log.e("EditMoodActivity", "Gagal: ${response.errorBody()?.string()}")
                        Toast.makeText(this@EditMoodActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<List<Mood>>, t: Throwable) {
                    Log.e("EditMoodActivity", "Error: ${t.message}")
                    Toast.makeText(this@EditMoodActivity, "Jaringan error: ${t.message}", Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
        }
    }

    private fun updateMood(moodId: String, newMood: String) {
        val moodUpdate = mapOf("mood" to newMood)
        Log.d("EditMoodActivity", "Updating mood with ID: $moodId, new mood: $newMood")
        apiService.updateMood("19B5K", "mood", moodId, moodUpdate).enqueue(object : Callback<Mood> {
            override fun onResponse(call: Call<Mood>, response: Response<Mood>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditMoodActivity, "Berhasil memperbarui mood", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@EditMoodActivity, RiwayatActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("EditMoodActivity", "Gagal: ${response.errorBody()?.string()}")
                    Toast.makeText(this@EditMoodActivity, "Gagal memperbarui mood", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Mood>, t: Throwable) {
                Log.e("EditMoodActivity", "Error: ${t.message}")
                Toast.makeText(this@EditMoodActivity, "Jaringan error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupMoodSelection() {
        val moodViews = listOf(binding.editImageSedih, binding.editImageMarah, binding.editImageBahagia)

        moodViews.forEach { imageView ->
            imageView.setOnClickListener {
                moodViews.forEach { it.setBackgroundResource(0) }

                imageView.setBackgroundResource(R.drawable.selected_background)

                selectedMoodString = when (imageView.id) {
                    R.id.edit_image_sedih -> "Sedih"
                    R.id.edit_image_marah -> "Marah"
                    R.id.edit_image_bahagia -> "Bahagia"
                    else -> null
                }

                Toast.makeText(this, "Mood $selectedMoodString dipilih", Toast.LENGTH_SHORT).show()

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
            }
        }
    }
}