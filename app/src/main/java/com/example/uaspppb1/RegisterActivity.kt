package com.example.uaspppb1

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.uaspppb1.DataBase.User
import com.example.uaspppb1.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // untuk mengatur warna status bar jadi putih
        window.statusBarColor = android.graphics.Color.WHITE
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding.register.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            val email = binding.email.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
                val idUser = generateRandomId()
                val user = User(id_user = idUser, username = username, password = password, email = email)
                lifecycleScope.launch {
                    (application as MyApp).getDatabase().userDao().insert(user)
                    Toast.makeText(this@RegisterActivity, "Berhasil, pengguna baru terdaftar", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateRandomId(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..8)
            .map { chars.random() }
            .joinToString("")
    }
}