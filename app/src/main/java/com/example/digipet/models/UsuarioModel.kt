package com.example.digipet.models

import java.io.Serializable

data class UsuarioModel(
    val id: Int,
    val email: String,
    val password: String,
    val gender: String
) : Serializable
