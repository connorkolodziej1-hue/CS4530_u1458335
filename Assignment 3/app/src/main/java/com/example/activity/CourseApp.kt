package com.example.activity

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.getValue
import androidx.room.Room
import com.example.activity.room.AppDatabase

class CourseApp: Application() {
    val scope = CoroutineScope(SupervisorJob())

    val db by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "AppDatabase").build()
    }

    val repository by lazy { Repository(scope, db.courseDao()) }

}