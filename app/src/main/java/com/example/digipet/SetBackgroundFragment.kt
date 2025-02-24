package com.example.digipet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.digipet.databinding.FragmentSetBackgroundBinding

class SetBackgroundFragment : Fragment() {

    // Interfaz para comunicar el color seleccionado a la actividad
    interface OnColorSelectedListener {
        fun onColorSelected(color: Int)
    }

    private var listener: OnColorSelectedListener? = null

    private var _binding: FragmentSetBackgroundBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verificamos que la actividad implemente la interfaz
        if (context is OnColorSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnColorSelectedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetBackgroundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración de los botones para cambiar el fondo usando viewBinding
        binding.btnRed.setOnClickListener {
            listener?.onColorSelected(android.graphics.Color.RED)
        }
        binding.btnBlue.setOnClickListener {
            listener?.onColorSelected(android.graphics.Color.BLUE)
        }
        binding.btnGreen.setOnClickListener {
            listener?.onColorSelected(android.graphics.Color.GREEN)
        }
        binding.btnYellow.setOnClickListener {
            listener?.onColorSelected(android.graphics.Color.YELLOW)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
