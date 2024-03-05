package com.example.tasklist

import android.content.Context
import androidx.room.Room
import com.example.tasklist.data.TaskDatabase
import com.example.tasklist.data.TaskRepository

object Graph {
    private lateinit var database: TaskDatabase

    val taskRepository by lazy {  // load the repository only when required (first time accessed)
        TaskRepository(taskDao = database.taskDao())
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, TaskDatabase::class.java, "tasklist-database").build()
    }
}