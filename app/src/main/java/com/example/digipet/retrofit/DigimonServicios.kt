package com.example.digipet.retrofit

import com.example.digipet.models.Digimon
import retrofit2.Call
import retrofit2.http.GET

interface DigimonServicios {
    @GET("api/digimon")
    fun getDigimons(): Call<List<Digimon>>
}
