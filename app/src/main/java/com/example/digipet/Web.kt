package com.example.digipet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment

class Web : Fragment() {

    private lateinit var webView: WebView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_web, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = view.findViewById(R.id.webView)

        val searchTerm = arguments?.getString("search_term")
        if (searchTerm != null) {
            loadSearchResults(searchTerm)
        } else {
            android.util.Log.e("WebFragment", "No se recibió ningún término de búsqueda")
        }

        // Configura un WebViewClient para manejar enlaces dentro del WebView
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                // Retorna false para que el WebView maneje la URL en lugar de derivarla al navegador
                return false
            }
        }

    }

    private fun loadSearchResults(query: String) {
        val url = "https://digimon.fandom.com/wiki/Special:Search?query=$query&scope=internal&navigationSearch=true"
        webView.loadUrl(url)

        // Configura las opciones del WebView
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webSettings.domStorageEnabled = true
    }
}
