package com.example.digipet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.digipet.databinding.ActivityPerfilBinding
import com.example.digipet.providers.db.CrudUsuarios

class Perfil : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    private val crudUsuarios = CrudUsuarios() // Instancia para interactuar con SQLite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar datos desde SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val email = sharedPreferences.getString("user_email", "Email no disponible")
        val password = sharedPreferences.getString("user_password", "Contraseña no disponible")

        // Mostrar datos en los TextViews
        binding.tvEmail.text = "Email: $email"
        binding.tvPassword.text = "Contraseña: $password"
        binding.btnBack.setOnClickListener { finish() }

        // Cargar datos adicionales desde SQLite (si hay información del género)
        val user = crudUsuarios.getUsuario(email!!)
        if (user != null) {
            binding.tvGender.text = "Género: ${user.gender}"
        } else {
            binding.tvGender.text = "Género: No disponible"
        }
    }
}
