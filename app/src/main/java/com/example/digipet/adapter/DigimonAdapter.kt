package com.example.digipet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.digipet.R
import com.example.digipet.models.Digimon
import com.squareup.picasso.Picasso

class DigimonAdapter(private val digimons: List<Digimon>) : RecyclerView.Adapter<DigimonAdapter.DigimonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigimonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.digimongaleria, parent, false)
        return DigimonViewHolder(view)
    }

    override fun onBindViewHolder(holder: DigimonViewHolder, position: Int) {
        val digimon = digimons[position]
        holder.name.text = digimon.name
        holder.level.text = digimon.level
        Picasso.get().load(digimon.img).into(holder.image)
    }

    override fun getItemCount(): Int {
        return digimons.size
    }

    class DigimonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.ivDigimonImage)
        val name: TextView = itemView.findViewById(R.id.tvName)
        val level: TextView = itemView.findViewById(R.id.tvLevel)
    }
}
