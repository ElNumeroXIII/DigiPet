package com.example.digipet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.digipet.databinding.FragmentWebBinding

class Web : Fragment() {

    private var _binding: FragmentWebBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchTerm = arguments?.getString("search_term")
        if (searchTerm != null) {
            loadSearchResults(searchTerm)
        } else {
            android.util.Log.e("WebFragment", "No se recibió ningún término de búsqueda")
        }

        // Configura un WebViewClient para manejar enlaces dentro del WebView
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                // Retorna false para que el WebView maneje la URL en lugar de delegarla al navegador
                return false
            }
        }
    }

    private fun loadSearchResults(query: String) {
        val url = "https://digimon.fandom.com/wiki/Special:Search?query=$query&scope=internal&navigationSearch=true"
        binding.webView.loadUrl(url)

        // Configura las opciones del WebView
        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webSettings.domStorageEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
