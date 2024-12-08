package com.example.digipet

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.digipet.adapter.DigimonAdapter
import com.example.digipet.databinding.ActivityGaleriaBinding
import com.example.digipet.models.Digimon
import com.example.digipet.retrofit.DigimonServicios
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Galeria : AppCompatActivity() {

    private lateinit var binding: ActivityGaleriaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el binding
        binding = ActivityGaleriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://digimon-api.vercel.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(DigimonServicios::class.java)

        // Llamada a la API
        service.getDigimons().enqueue(object : Callback<List<Digimon>> {
            override fun onResponse(call: Call<List<Digimon>>, response: Response<List<Digimon>>) {
                if (response.isSuccessful) {
                    val digimons = response.body() ?: emptyList()
                    setupRecyclerView(digimons)
                } else {
                    Toast.makeText(this@Galeria, "Error en la respuesta", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Digimon>>, t: Throwable) {
                Toast.makeText(this@Galeria, "Error de red", Toast.LENGTH_SHORT).show()
            }
        })

        // Configurar el botón "Volver Atrás"
        val btnBack: Button = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            // Al hacer clic en el botón, volver a la actividad anterior (pokedex)
            onBackPressed()
        }
    }

    private fun setupRecyclerView(digimons: List<Digimon>) {
        binding.recyclerView.layoutManager = GridLayoutManager(this,2)
        binding.recyclerView.adapter = DigimonAdapter(digimons)
    }
}


