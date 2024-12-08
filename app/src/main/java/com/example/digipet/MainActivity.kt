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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val crudUsuarios = CrudUsuarios() // Instancia de CRUD para usuarios
    private val RC_SIGN_IN = 9001 // Código para Google Sign-In

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa Firebase Auth
        auth = Firebase.auth

        // Configura Google Sign-In
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Debe estar en google-services.json
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // Obtener el correo del intent (si viene de otro activity)
        val userEmail = intent.getStringExtra("USER_EMAIL")
        val emailEditText = findViewById<EditText>(R.id.tfUsuario)

        if (userEmail != null) {
            emailEditText.setText(userEmail) // Mostrar el correo recibido
        }

        // Listeners para botones
        findViewById<Button>(R.id.btExit).setOnClickListener { finish() }
        findViewById<Button>(R.id.btLogin).setOnClickListener { loginUser() }
        findViewById<Button>(R.id.btRegister).setOnClickListener { registerUser() }
        findViewById<com.google.android.gms.common.SignInButton>(R.id.signInButton).setOnClickListener {
            googleSignIn()
        }
    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Obtiene los datos del usuario de Google
                    val userEmail = account.email ?: "unknown@gmail.com"

                    // Guardar datos en SQLite y SharedPreferences
                    saveGoogleUserData(userEmail)

                    // Navegar a la Pokedex
                    navigateToPokedex()
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveGoogleUserData(email: String) {
        // Guardar en SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_email", email)
        editor.putString("user_password", "ninguna")
        editor.putString("user_option", "Desconocido") // Opción 3
        editor.putBoolean("terms_accepted", true) // Aceptó términos
        editor.apply()

        // Crear un objeto UsuarioModel para SQLite
        val usuario = UsuarioModel(
            id = 0, // Asume que la base de datos genera el ID automáticamente
            email = email,
            password = "ninguna",
            gender = "Desconocido"
        )

        // Guardar en SQLite usando el método create de CrudUsuarios
        val resultado = crudUsuarios.create(usuario)
        if (resultado == -1L) {
            Toast.makeText(this, "Error al guardar el usuario en SQLite", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Usuario guardado exitosamente en SQLite", Toast.LENGTH_SHORT).show()
        }
    }


    private fun registerUser() {
        val email = findViewById<EditText>(R.id.tfUsuario).text.toString()
        val password = findViewById<EditText>(R.id.tfPassword).text.toString()

        // Validaciones básicas de entrada
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    loginUser()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun loginUser() {
        val email = findViewById<EditText>(R.id.tfUsuario).text.toString()
        val password = findViewById<EditText>(R.id.tfPassword).text.toString()

        // Validaciones básicas de entrada
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                    // Guardar datos en SQLite y SharedPreferences
                    saveUserData(email, password)

                    // Navegar a la Pokedex
                    navigateToPokedex()
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserData(email: String, password: String) {
        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_email", email)
        editor.putString("user_password", password)
        editor.apply()  // Guarda los cambios
    }

    private fun navigateToPokedex() {
        val email = findViewById<EditText>(R.id.tfUsuario).text.toString()
        val intent = Intent(this, PokedexActivity::class.java)
        intent.putExtra("USER_EMAIL", email)
        startActivity(intent)
        finish()
    }
}
