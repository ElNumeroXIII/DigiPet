package com.example.digipet


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.digipet.models.UsuarioModel
import com.example.digipet.providers.db.CrudUsuarios
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val crudUsuarios = CrudUsuarios() // Instancia de CRUD para usuarios

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa Firebase
        Firebase.initialize(this)
        auth = Firebase.auth

        // Listeners para botones
        findViewById<Button>(R.id.btExit).setOnClickListener { finish() }
        findViewById<Button>(R.id.btLogin).setOnClickListener { loginUser() }
        findViewById<Button>(R.id.btRegister).setOnClickListener { registerUser() }
    }

    private fun registerUser() {
        val email = findViewById<EditText>(R.id.tfUsuario).text.toString()
        val password = findViewById<EditText>(R.id.tfPassword).text.toString()

        // Validaciones básicas de entrada
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Verificar si el género está seleccionado
        val gender = when {
            findViewById<RadioButton>(R.id.rbA).isChecked -> "Puede"
            findViewById<RadioButton>(R.id.rbB).isChecked -> "En ocasiones especiales"
            findViewById<RadioButton>(R.id.rbC).isChecked -> "Otros"
            else -> {
                Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
                return
            }

        }

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    //Todo ha ido bien, llamamos a login
                    loginUser()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
            }

    }

    // Función para manejar el login
    private fun loginUser() {
        val email = findViewById<EditText>(R.id.tfUsuario).text.toString()
        val password = findViewById<EditText>(R.id.tfPassword).text.toString()

        // Validaciones básicas de entrada
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Verificar si el género está seleccionado
        val gender = when {
            findViewById<RadioButton>(R.id.rbA).isChecked -> "Puede"
            findViewById<RadioButton>(R.id.rbB).isChecked -> "En ocasiones especiales"
            findViewById<RadioButton>(R.id.rbC).isChecked -> "Otros"
            else -> {
                Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Intentar iniciar sesión con Firebase
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                    // Guardar los datos en SharedPreferences y SQLite
                    saveUserData(email, password, gender)
                    saveUserToSQLite(email, password, gender)

                    // Navegar a la Pokedex
                    navigateToPokedex()
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Guardar datos en SharedPreferences
    private fun saveUserData(email: String, password: String, gender: String) {
        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_email", email)
        editor.putString("user_password", password)
        editor.putString("user_gender", gender)
        editor.apply() // Guarda los cambios
    }

    // Guardar datos en SQLite
    private fun saveUserToSQLite(email: String, password: String, gender: String) {
        val usuario = UsuarioModel(id = 0, email = email, password = password, gender = gender)
        val result = crudUsuarios.create(usuario)
        if (result != -1L) {
            Toast.makeText(this, "User saved in SQLite", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to save user in SQLite", Toast.LENGTH_SHORT).show()
        }
    }

    // Navegar a la actividad Pokedex
    private fun navigateToPokedex() {
        val intent = Intent(this, PokedexActivity::class.java)
        startActivity(intent)
        finish()
    }
}