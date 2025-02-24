package com.example.digipet

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.lifecycle.lifecycleScope
import com.example.digipet.adapter.DigimonAdapter
import com.example.digipet.databinding.ActivityGaleriaBinding
import com.example.digipet.models.Digimon
import com.example.digipet.retrofit.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Galeria : AppCompatActivity() {

    private lateinit var binding: ActivityGaleriaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGaleriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Llamar a la API con Coroutines
        traerDigimons()

        // Configurar el botón "Volver Atrás"
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun traerDigimons() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val digimons = ApiClient.apiService.getDigimons()
                withContext(Dispatchers.Main) {
                    setupRecyclerView(digimons)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Galeria, "Error al cargar Digimons", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRecyclerView(digimons: List<Digimon>) {
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = DigimonAdapter(digimons)
    }
}
