package com.hotmail.example.proyecto_mov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.hotmail.example.proyecto_mov.databinding.ActivityHomeBinding

enum class ProviderType{
    BASIC
}

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle: Bundle?=intent.extras
        val email : String? = bundle?.getString("email")
        val provider: String? = bundle?.getString("provider")
        setUp(email?:"",provider?:"")
    }

    private fun setUp(email : String, provider:String) {
        title = "Inicio"
        binding.emailTextView.text= email
        binding.providerTextView.text=provider
        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }
}