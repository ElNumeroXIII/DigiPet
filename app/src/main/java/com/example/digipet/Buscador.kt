package com.example.digipet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.digipet.databinding.FragmentBuscadorBinding

class Buscador : Fragment() {

    private var _binding: FragmentBuscadorBinding? = null
    private val binding get() = _binding!!

    private var searchListener: OnSearchListener? = null

    interface OnSearchListener {
        fun onSearch(query: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuscadorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración del SearchView usando viewBinding
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchListener?.onSearch(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSearchListener) {
            searchListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        searchListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
