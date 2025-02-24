package com.example.digipet.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digipet.databinding.TareaBinding
import com.example.digipet.models.Tarea

class TaskAdapter(
    private var tasks: List<Tarea>,
    private val onTaskChecked: (Tarea, Boolean) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TareaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int = tasks.size

    inner class TaskViewHolder(private val binding: TareaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Tarea) {
            binding.tvTaskDescription.text = task.description
            binding.cbTaskCompleted.isChecked = task.completed

            binding.cbTaskCompleted.setOnCheckedChangeListener { _, isChecked ->
                onTaskChecked(task, isChecked)
            }
        }
    }
}

