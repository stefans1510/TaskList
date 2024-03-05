package com.example.tasklist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasklist.data.Task
import com.example.tasklist.data.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository = Graph.taskRepository): ViewModel() {
    var taskTitleState by mutableStateOf("")
    var taskDescriptionState by mutableStateOf("")
    var taskStatusState by mutableStateOf("")

    fun onTaskTitleChanged(newString: String) {
        taskTitleState = newString
    }

    fun onTaskDescriptionChanged(newString: String) {
        taskDescriptionState = newString
    }

    fun onTaskStatusChanged(newString: String) {
        taskStatusState = newString
    }

    lateinit var getAllTasks: Flow<List<Task>>

    fun getTaskById(id:Long):Flow<Task> {
        return taskRepository.getTaskById(id)
    }

    init {
        viewModelScope.launch {
            getAllTasks = taskRepository.getAllTasks()
        }
    }

    fun createTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.createTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.deleteTask(task)
        }
    }
}
