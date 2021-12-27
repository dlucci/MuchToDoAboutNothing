package com.dlucci.myapplication

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single


@Database(entities = [Task::class], version = 1)
abstract class RoomDbWrapper : RoomDatabase(){
    abstract fun roomDao() : RoomDao
}

@Dao
interface RoomDao {

    @Query("Select * from Task")
    fun selectAll() : Observable<List<Task>>

    @Insert
    fun insert(task : Task) : Completable

    @Query("Select * from Task Where item LIKE :item")
    fun query(item : String?) : Observable<List<Task>>
}