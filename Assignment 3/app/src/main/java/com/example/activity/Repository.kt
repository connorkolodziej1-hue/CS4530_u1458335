package com.example.activity

import com.example.activity.room.CourseDaoInterface
import com.example.activity.room.CourseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class Repository (val scope: CoroutineScope, private val dao: CourseDaoInterface){

    val allCourses: Flow<List<CourseEntity?>> = dao.getAllCourses()


    fun addCourse(course: CourseEntity){
        scope.launch {
            dao.insertCourse(course)
        }
    }

    fun removeCourse(course: CourseEntity) {
        scope.launch {
            dao.deleteCourse(course)
        }
    }
}
