package com.example.digipet.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://digimon-api.vercel.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: DigimonServicios = retrofit.create(DigimonServicios::class.java)
}

