package com.example.digipet

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.digipet.databinding.ActivityMapasBinding
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

class Mapas : AppCompatActivity() {

    private lateinit var binding: ActivityMapasBinding
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración inicial de osmdroid
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))

        // Inicializar el MapView y habilitar controles multitáctiles
        binding.map.setMultiTouchControls(true)

        // Comprobar si tenemos el permiso de ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            showUserLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        // Configurar el botón "Volver Atrás"
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    // Método para mostrar la ubicación del usuario en el mapa
    private fun showUserLocation() {
        val latitude = 36.85031587311024  // Coordenadas de ejemplo (Madrid)
        val longitude = -2.465100359721841
        val currentLocation = GeoPoint(latitude, longitude)

        binding.map.controller.setZoom(15.0)
        binding.map.controller.setCenter(currentLocation)

        // Añadir un marcador en la ubicación actual
        val marker = Marker(binding.map)
        marker.position = currentLocation
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Mi posición"
        binding.map.overlays.add(marker)
    }

    // Manejar la respuesta a la solicitud de permisos
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showUserLocation()
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }
}

