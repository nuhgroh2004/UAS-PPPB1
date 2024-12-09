package com.example.uaspppb1

import android.content.Intent
import android.os.Bundle
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uaspppb1.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val imageSedih = findViewById<ImageView>(R.id.image_sedih)
        val imageMarah = findViewById<ImageView>(R.id.image_marah)
        val imageBahagia = findViewById<ImageView>(R.id.image_bahagia)

        setupClickAnimation(imageSedih)
        setupClickAnimation(imageMarah)
        setupClickAnimation(imageBahagia)

        binding.profilSection.setOnClickListener {
            val intent = Intent(this, ProfilActivity::class.java)
            startActivity(intent)
        }

    }
    private fun setupClickAnimation(imageView: ImageView) {
        imageView.setOnClickListener {
            val scaleUp = ScaleAnimation(
                1f, 1.2f, // From X scale to X scale
                1f, 1.2f, // From Y scale to Y scale
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f, // Pivot X
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f  // Pivot Y
            ).apply {
                duration = 100 // Animation duration
                fillAfter = true // Keep the final state
            }

            val scaleDown = ScaleAnimation(
                1.2f, 1f,
                1.2f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                startOffset = 100 // Start after scale up
                duration = 100
                fillAfter = true
            }

            imageView.startAnimation(scaleUp)
            imageView.postDelayed({ imageView.startAnimation(scaleDown) }, 100)
        }
    }

}