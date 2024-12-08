package com.example.digipet

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class Mapas : AppCompatActivity() {

    private lateinit var mapView: MapView
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapas)

        // Configuración inicial de osmdroid
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))

        // Inicializar el MapView
        mapView = findViewById(R.id.map)
        mapView.setMultiTouchControls(true)

        // Comprobar si tenemos el permiso de ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Si tenemos permiso, configurar la ubicación y mostrarla en el mapa
            showUserLocation()
        } else {
            // Si no tenemos el permiso, solicitarlo
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }

        // Configurar el botón "Volver Atrás"
        val btnBack: Button = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            // Al hacer clic en el botón, volver a la actividad anterior (pokedex)
            onBackPressed()
        }

    }

    // Método para mostrar la ubicación del usuario en el mapa
    private fun showUserLocation() {
        val latitude = 36.85031587311024  // Coordenadas de ejemplo (Madrid)
        val longitude = -2.465100359721841
        val currentLocation = GeoPoint(latitude, longitude)

        mapView.controller.setZoom(15.0)
        mapView.controller.setCenter(currentLocation)

        // Añadir un marcador en la ubicación actual
        val marker = Marker(mapView)
        marker.position = currentLocation
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Mi posición"
        mapView.overlays.add(marker)
    }

    // Manejar la respuesta a la solicitud de permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si se concede el permiso, mostrar la ubicación
                showUserLocation()
            } else {
                // Si se deniega el permiso, puedes mostrar un mensaje o realizar otra acción
                // Por ejemplo, mostrar un Toast informando de que el permiso es necesario
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }


}


