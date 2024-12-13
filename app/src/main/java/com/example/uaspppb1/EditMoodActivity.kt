// EditMoodActivity.kt
package com.example.uaspppb1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.uaspppb1.databinding.ActivityEditMoodBinding

class EditMoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditMoodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditMoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            val intent = Intent(this, RiwayatActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}