package com.example.rutasvivas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        tvError = findViewById(R.id.tvError)

        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        val btnIrRegistro = findViewById<Button>(R.id.btnIrRegistro)

        btnIngresar.setOnClickListener { iniciarSesion() }

        btnIrRegistro.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            irACatalogo()
        }
    }

    private fun iniciarSesion() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            tvError.text = getString(R.string.error_campo_vacio)
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { irACatalogo() }
            .addOnFailureListener { e ->
                tvError.text = e.localizedMessage ?: "Error al iniciar sesión"
            }
    }

    private fun irACatalogo() {
        startActivity(Intent(this, CatalogoActivity::class.java))
        finish()
    }
}


