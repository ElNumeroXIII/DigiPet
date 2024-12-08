package com.example.digipet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class SetBackgroundFragment : Fragment() {

    // Interfaz para comunicar el color seleccionado a la actividad
    interface OnColorSelectedListener {
        fun onColorSelected(color: Int)
    }

    private var listener: OnColorSelectedListener? = null

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
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_set_background, container, false)

        // Configuración de los botones para cambiar el fondo
        view.findViewById<Button>(R.id.btnRed).setOnClickListener {
            listener?.onColorSelected(android.graphics.Color.RED)
        }
        view.findViewById<Button>(R.id.btnBlue).setOnClickListener {
            listener?.onColorSelected(android.graphics.Color.BLUE)
        }
        view.findViewById<Button>(R.id.btnGreen).setOnClickListener {
            listener?.onColorSelected(android.graphics.Color.GREEN)
        }
        view.findViewById<Button>(R.id.btnYellow).setOnClickListener {
            listener?.onColorSelected(android.graphics.Color.YELLOW)
        }

        return view
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
