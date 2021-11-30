package com.dlucci.myapplication

import android.content.DialogInterface
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlucci.myapplication.databinding.ActivityMainBinding
import com.dlucci.myapplication.databinding.AddTaskDialogFragmentBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter : TaskAdapter


    //store this in a list in the short term until the DB is set up
    private var list = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVariables()
        setContentView(binding.root)
        initListeners()
    }

    fun initVariables() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        adapter = TaskAdapter(list)
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter
    }

    fun initListeners() {
        binding.fab.setOnClickListener { view ->
            //var dialog = AddTaskDialog(this)
            val dialogBinding = AddTaskDialogFragmentBinding.inflate(layoutInflater)
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("New Task")
            dialog.setView(dialogBinding.root)
            dialog.setPositiveButton("ok"
            ) { dialog, which -> addToList(dialogBinding.task.text.toString()) }
            dialog.setNegativeButton("cancel"
            ) { dialog, which -> dialog?.dismiss() }
            dialog.create().show()

        }
    }

    private fun addToList(task: String?) {
        if(task?.isEmpty() ?: true) {
            return
        }

        list.add(task ?: "")
        adapter.notifyDataSetChanged()
    }
}