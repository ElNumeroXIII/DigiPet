package com.example.digipet.providers.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.digipet.Aplicacion
import com.example.digipet.models.UsuarioModel

public class CrudUsuarios {

    // Crear un nuevo usuario
     fun create(usuario: UsuarioModel): Long {
        val db = Aplicacion.llave.writableDatabase
        return try {
            db.insertWithOnConflict(
                Aplicacion.TABLA,
                null,
                usuario.toContentValues(),
                SQLiteDatabase.CONFLICT_IGNORE
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            -1L
        } finally {
            db.close()
        }
    }

    // Leer todos los usuarios
    fun read(): MutableList<UsuarioModel> {
        val lista = mutableListOf<UsuarioModel>()
        val db = Aplicacion.llave.readableDatabase
        try {
            val cursor = db.query(
                Aplicacion.TABLA,
                arrayOf("id", "email", "password", "gender"),
                null,
                null,
                null,
                null,
                null
            )
            while (cursor.moveToNext()) {
                val usuario = UsuarioModel(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
                )
                lista.add(usuario)
            }
            cursor.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            db.close()
        }
        return lista
    }

    // Actualizar un usuario existente
    fun update(usuario: UsuarioModel): Boolean {
        val db = Aplicacion.llave.writableDatabase
        val values = usuario.toContentValues()
        return try {
            val filasAfectadas = db.update(
                Aplicacion.TABLA,
                values,
                "id = ?",
                arrayOf(usuario.id.toString())
            )
            filasAfectadas > 0
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        } finally {
            db.close()
        }
    }

    // Borrar un usuario por ID
    fun delete(id: Int): Boolean {
        val db = Aplicacion.llave.writableDatabase
        return try {
            val filasEliminadas = db.delete(Aplicacion.TABLA, "id = ?", arrayOf(id.toString()))
            filasEliminadas > 0
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        } finally {
            db.close()
        }
    }

    // Borrar todos los usuarios
    fun deleteAll() {
        val db = Aplicacion.llave.writableDatabase
        try {
            db.execSQL("DELETE FROM ${Aplicacion.TABLA}")
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            db.close()
        }
    }

    // Extensión para convertir un UsuarioModel a ContentValues
    private fun UsuarioModel.toContentValues(): ContentValues {
        return ContentValues().apply {
            put("email", email)
            put("password", password)
            put("gender", gender)
        }
    }

    // Obtener un usuario por email
    fun getUsuario(email: String): UsuarioModel? {
        val db = Aplicacion.llave.readableDatabase
        return try {
            val cursor = db.query(
                Aplicacion.TABLA,
                arrayOf("id", "email", "password", "gender"),
                "email = ?",
                arrayOf(email),
                null,
                null,
                null
            )
            if (cursor.moveToFirst()) {
                val usuario = UsuarioModel(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
                )
                cursor.close()
                usuario
            } else {
                cursor.close()
                null
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        } finally {
            db.close()
        }
    }

}
