package com.example.tes_cbig.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.tes_cbig.main.MainActivity
import com.example.tes_cbig.R

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi view
        val etEmail = findViewById<EditText>(R.id.email)
        val etPassword = findViewById<EditText>(R.id.password)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // Menambahkan aksi pada tombol login
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            // Panggil fungsi login
            loginViewModel.login(email, password)
        }

        loginViewModel.loginResult.observe(this, Observer { response ->
            if (response.statusCode == 1) {
                // Login berhasil, pindah ke HomeActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Tutup MainActivity agar tidak bisa kembali dengan tombol back

            }else {
                Toast.makeText(this, "Login Gagal: ${response.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}