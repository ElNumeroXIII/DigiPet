package com.example.digipet

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.digipet.databinding.ActivityBusquedaBinding

class Busqueda : AppCompatActivity(), Buscador.OnSearchListener {

    private lateinit var binding: ActivityBusquedaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusquedaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar el SearchFragment
        if (savedInstanceState == null) {
            val searchFragment = Buscador()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(binding.fragmentContainerSearch.id, searchFragment)
            transaction.commit()
        }

        binding.btnBack.setOnClickListener {
            // Al hacer clic en el botón, volver a la actividad anterior (pokedex)
            onBackPressed()
        }
    }

    override fun onSearch(query: String) {
        // Depuración: Verifica si el listener está funcionando
        android.util.Log.d("Busqueda", "Término de búsqueda: $query")
        Toast.makeText(this, "Buscando: $query", Toast.LENGTH_SHORT).show()

        // Cargar el fragmento WebView
        val webViewFragment = Web()
        val bundle = Bundle()
        bundle.putString("search_term", query)
        webViewFragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragmentContainerWebview.id, webViewFragment)
        transaction.commit()
    }
}

