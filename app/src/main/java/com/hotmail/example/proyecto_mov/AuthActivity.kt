package com.hotmail.example.proyecto_mov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.hotmail.example.proyecto_mov.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    private fun setup() {
        title = "Authentication"
        binding.signUpButton.setOnClickListener {
            if (binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.emailEditText.text.toString(), binding.passwordEditText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        showAlert()
                    }
                }
            }else{
                if ("".equals(binding.emailEditText.text.toString())) {
                    binding.emailEditText.error = "Ingresar correo"
                }
                if ("".equals(binding.passwordEditText.text.toString())) {
                    binding.passwordEditText.error = "Ingresar contraseña"
                }
            }
        }

        binding.loginButton.setOnClickListener {
            if(binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.emailEditText.text.toString(),binding.passwordEditText.text.toString()
                ).addOnCompleteListener {
                    if(it.isSuccessful){
                        showNotas()
                        binding.passwordEditText.setText("")
                    }else{
                        showAlert()
                    }
                }
            }else{
                if ("".equals(binding.emailEditText.text.toString())) {
                    binding.emailEditText.error = "Ingresar correo"
                }
                if ("".equals(binding.passwordEditText.text.toString())) {
                    binding.passwordEditText.error = "Ingresar contraseña"
                }
            }
        }
    }

    private fun showHome(email: String?, provider: ProviderType) {
        val homeIntent = Intent(this,HomeActivity::class.java).apply {
            putExtra("email",email)
            putExtra("provider",provider.name)
        }
        startActivity(homeIntent)
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setMessage("Se ha producido un error al autentificar usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }

    private fun showNotas() {
        val notasIntent = Intent(this,NotasActivity::class.java)
        startActivity(notasIntent)
    }
}