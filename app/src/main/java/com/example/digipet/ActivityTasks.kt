package com.example.digipet

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.digipet.adapters.TaskAdapter
import com.example.digipet.databinding.ActivityTasksBinding
import com.example.digipet.models.Tarea
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference

class ActivityTasks : AppCompatActivity() {

    private lateinit var binding: ActivityTasksBinding
    private lateinit var database: DatabaseReference
    private lateinit var adapter: TaskAdapter
    private var taskList = mutableListOf<Tarea>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance("https://digidex-3b25d-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("tasks")



        setupRecyclerView()
        loadTasks()

        binding.btnAddTask.setOnClickListener {
            addTask()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter(taskList) { task, isChecked ->
            updateTaskStatus(task, isChecked)
        }
        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        binding.rvTasks.adapter = adapter
    }

    private fun loadTasks() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                taskList.clear()
                for (taskSnapshot in snapshot.children) {
                    val task = taskSnapshot.getValue(Tarea::class.java)
                    task?.let { taskList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ActivityTasks, "Error al cargar tareas", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addTask() {
        Log.d("DEBUG_FIREBASE", "addTask() ejecutado") // Comprobar si la función se ejecuta

        val taskDescription = binding.etNewTask.text.toString().trim()
        if (taskDescription.isEmpty()) {
            Log.d("DEBUG_FIREBASE", "El campo de texto está vacío") // Verificar si detecta campo vacío
            Toast.makeText(this, "Escribe una tarea", Toast.LENGTH_SHORT).show()
            return
        }

        val taskId = database.push().key
        if (taskId == null) {
            Log.e("DEBUG_FIREBASE", "Error al generar Task ID") // Si Firebase falla al generar un ID
            return
        }

        val task = Tarea(taskId, taskDescription, false)
        Log.d("DEBUG_FIREBASE", "Guardando tarea: $task") // Ver qué datos se intentan guardar

        database.child(taskId).setValue(task)
            .addOnCompleteListener {
                Log.d("DEBUG_FIREBASE", "Tarea guardada exitosamente") // Si se guarda bien
                binding.etNewTask.text.clear()
                Toast.makeText(this, "Tarea añadida", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("DEBUG_FIREBASE", "Error al agregar tarea: ${e.message}") // Si Firebase rechaza la escritura
                Toast.makeText(this, "Error al agregar tarea: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }



    private fun updateTaskStatus(task: Tarea, isChecked: Boolean) {
        database.child(task.id).child("completed").setValue(isChecked)
    }
}
