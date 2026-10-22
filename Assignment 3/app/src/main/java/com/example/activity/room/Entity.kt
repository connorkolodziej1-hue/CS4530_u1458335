package com.example.activity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    val department: String,
    val courseNumber: Int,
    val location: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)