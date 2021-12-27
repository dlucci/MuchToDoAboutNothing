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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.dlucci.myapplication.databinding.ActivityMainBinding
import com.dlucci.myapplication.databinding.AddTaskDialogFragmentBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.ArrayCompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter : TaskAdapter

    private lateinit var db : RoomDbWrapper

    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var disposable : Disposable

    //store this in a list in the short term until the DB is set up
    private var list = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        initVariables()
        initListeners()
    }

    fun initVariables() {
        db = Room.databaseBuilder(this, RoomDbWrapper::class.java, "database").build()
        compositeDisposable = CompositeDisposable()
        populateRecyclerView()
    }

    private fun populateRecyclerView() {

        disposable = db.roomDao()
            .selectAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                list = it?.toMutableList() ?: mutableListOf()
                adapter = TaskAdapter(list)
                binding.recycler.layoutManager = LinearLayoutManager(this)
                binding.recycler.adapter = adapter
            }

        compositeDisposable.add(disposable)
    }

    fun initListeners() {
        binding.fab.setOnClickListener { view ->
            val dialogBinding = AddTaskDialogFragmentBinding.inflate(layoutInflater)
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("New Task")
            dialog.setView(dialogBinding.root)
            dialog.setPositiveButton("ok"
            ) { _, which -> addToList(dialogBinding.task.text.toString()) }
            dialog.setNegativeButton("cancel"
            ) { dialog, which -> dialog?.dismiss() }
            dialog.create().show()

        }
    }

    private fun addToList(task: String?) {
        if(task?.isEmpty() != false) {
            return
        }

        db.roomDao().insert(Task(task))
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { disposable -> compositeDisposable.add(disposable) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

    }
}