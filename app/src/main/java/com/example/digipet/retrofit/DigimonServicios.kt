package com.example.digipet.retrofit

import com.example.digipet.models.Digimon
import retrofit2.http.GET

interface DigimonServicios {
    @GET("api/digimon")
    suspend fun getDigimons(): List<Digimon>
}

