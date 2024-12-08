package com.example.digipet

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.digipet.providers.db.CrudUsuarios

class Perfil : AppCompatActivity() {

    private val crudUsuarios = CrudUsuarios() // Instancia para interactuar con SQLite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Obtener vistas
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val tvPassword = findViewById<TextView>(R.id.tvPassword)
        val tvGender = findViewById<TextView>(R.id.tvGender)

        // Cargar datos desde SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val email = sharedPreferences.getString("user_email", "Email no disponible")
        val password = sharedPreferences.getString("user_password", "Contraseña no disponible")

        // Mostrar datos en los TextViews
        tvEmail.text = "Email: $email"
        tvPassword.text = "Contraseña: $password"

        // Cargar datos adicionales desde SQLite (si hay información del género)
        val user = crudUsuarios.getUsuario(email!!)
        if (user != null) {
            tvGender.text = "Género: ${user.gender}"
        } else {
            tvGender.text = "Género: No disponible"
        }
    }
}
