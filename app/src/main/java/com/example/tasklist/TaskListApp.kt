package com.example.tasklist

import android.app.Application

class TaskListApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}