package com.example.digipet

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.digipet.databinding.ActivityPokedexBinding
import com.google.firebase.auth.FirebaseAuth

class PokedexActivity : AppCompatActivity(), SetBackgroundFragment.OnColorSelectedListener {

    private lateinit var binding: ActivityPokedexBinding
    private lateinit var auth: FirebaseAuth

    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokedexBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Obtener el correo del intent
        val userEmail = intent.getStringExtra("USER_EMAIL")
        if (userEmail != null) {
            binding.tvSubMenu.text = "Bienvenido, $userEmail"
        }

        // Configurar el botón de logout
        binding.btLogout.setOnClickListener {
            // Cerrar sesión en Firebase
            auth.signOut()

            // Volver a MainActivity y pasar el correo del usuario
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USER_EMAIL", userEmail)  // Mandamos el correo
            startActivity(intent)
            finish()
        }

        binding.btBuscadorWiki.setOnClickListener {
            // Crear un intent para iniciar la actividad Busqueda
            val intent = Intent(this, Busqueda::class.java)
            startActivity(intent)
        }

        binding.btMapa.setOnClickListener {
            // Comprobar si se tienen los permisos de ubicación
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Si los permisos ya están concedidos, iniciar la actividad del mapa
                startMapActivity()
            } else {
                // Si los permisos no están concedidos, solicitarlos
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
            }
        }

        binding.btGaleria.setOnClickListener{
            val intent = Intent(this, Galeria::class.java)
            startActivity(intent)
        }

        binding.btProfile.setOnClickListener{
            val intent = Intent(this, Perfil::class.java)
            startActivity(intent)
        }
    }

    // Función para iniciar la actividad del mapa
    private fun startMapActivity() {
        val intent = Intent(this, Mapas::class.java)
        startActivity(intent)
    }

    // Método para manejar la respuesta a la solicitud de permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si el permiso es concedido, iniciar la actividad del mapa
                startMapActivity()
            } else {
                // Si el permiso es denegado, mostrar un mensaje
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onColorSelected(color: Int) {
        // Cambiar el fondo del layout principal
        binding.main.background.setTint(color)
    }
}



