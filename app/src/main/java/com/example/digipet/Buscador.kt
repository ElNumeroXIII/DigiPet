package com.example.digipet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment

class Buscador : Fragment() {

    private lateinit var searchView: SearchView
    private var searchListener: OnSearchListener? = null

    interface OnSearchListener {
        fun onSearch(query: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_buscador, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = view.findViewById(R.id.searchView)

        // Configuración del SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchListener?.onSearch(it)  // Pasa el término de búsqueda al fragmento WebView
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if (context is OnSearchListener) {
            searchListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        searchListener = null
    }
}
