package com.example.tasklist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "status")
    val status: String
)

object DummyData {
    val tasks = listOf(
        Task(title = "Task1", description = "Some text...", status = "Pending"),
        Task(title = "Task2", description = "Some text...", status = "Completed"),
        Task(title = "Task3", description = "Some text...", status = "Canceled")
    )
}
