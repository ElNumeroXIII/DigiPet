package com.example.digipet

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

class Busqueda : AppCompatActivity(), Buscador.OnSearchListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_busqueda)

        // Cargar el SearchFragment
        if (savedInstanceState == null) {
            val searchFragment = Buscador()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_search, searchFragment)
            transaction.commit()
        }

        val btnBack: Button = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            // Al hacer clic en el botón, volver a la actividad anterior (pokedex)
            onBackPressed()
        }

    }

    override fun onSearch(query: String) {
        // Depuración: Verifica si el listener está funcionando
        android.util.Log.d("Busqueda", "Término de búsqueda: $query")
        // O usar un Toast para depuración visual
        Toast.makeText(this, "Buscando: $query", Toast.LENGTH_SHORT).show()

        // Cargar el fragmento WebView
        val webViewFragment = Web()
        val bundle = Bundle()
        bundle.putString("search_term", query)
        webViewFragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_webview, webViewFragment)
        transaction.commit()
    }

}
