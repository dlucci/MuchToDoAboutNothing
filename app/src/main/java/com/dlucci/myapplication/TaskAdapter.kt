package com.dlucci.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dlucci.myapplication.databinding.TaskRowBinding

class TaskAdapter(val data : List<String>) : RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskRowBinding.inflate(LayoutInflater.from(parent.context))
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.binding.task.text = data[position]
    }

    override fun getItemCount() = data.size

}

class TaskViewHolder(val binding : TaskRowBinding) : RecyclerView.ViewHolder(binding.root)
