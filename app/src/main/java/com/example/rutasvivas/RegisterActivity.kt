package com.example.rutasvivas

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val tvError = findViewById<TextView>(R.id.tvError)
        val btnCrear = findViewById<Button>(R.id.btnCrear)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        btnCrear.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                tvError.text = getString(R.string.error_campo_vacio)
                return@setOnClickListener
            }
            if (password.length < 6) {
                tvError.text = "La contraseña debe tener mínimo 6 caracteres"
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    startActivity(android.content.Intent(this, CatalogoActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    tvError.text = e.localizedMessage ?: "Error al registrar"
                }
        }

        btnVolver.setOnClickListener { finish() }
    }
}