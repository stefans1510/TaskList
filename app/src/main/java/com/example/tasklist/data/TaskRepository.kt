package com.example.tasklist.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    suspend fun createTask(task: Task) {
        taskDao.createTask(task)
    }

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    fun getTaskById(id: Long): Flow<Task> {
        return taskDao.getTaskById(id)
    }

    suspend fun updateTask(task: Task) {
        return taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        return taskDao.deleteTask(task)
    }
}