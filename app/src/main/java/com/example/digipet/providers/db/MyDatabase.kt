package com.example.digipet.providers.db

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.digipet.Aplicacion

class MyDatabase : SQLiteOpenHelper(Aplicacion.contexto, Aplicacion.DB, null, Aplicacion.VERSION) {

    private val q = """
        CREATE TABLE ${Aplicacion.TABLA} (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            email TEXT NOT NULL UNIQUE,
            password TEXT NOT NULL,
            gender TEXT NOT NULL
        );
    """.trimIndent()

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(q)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion) {
            val borrarTabla = "DROP TABLE IF EXISTS ${Aplicacion.TABLA}"
            db?.execSQL(borrarTabla)
            onCreate(db)
        }
    }
}
