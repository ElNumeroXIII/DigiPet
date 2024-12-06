package com.example.digipet

import android.app.Application
import android.content.Context
import com.example.digipet.providers.db.MyDatabase

class Aplicacion : Application() {

    companion object {
        lateinit var contexto: Context
        lateinit var llave: MyDatabase

        const val DB = "digipet_db"
        const val VERSION = 1
        const val TABLA = "user_table"
    }

    override fun onCreate() {
        super.onCreate()
        contexto = applicationContext
        llave = MyDatabase()
    }
}
