package com.example.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.activity.ui.theme.MVVMDemoV2Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

//viewModel and the Model (The model is just a list of items, that's why I didn't create a separate class/file for it
class CourseViewModel : ViewModel()
{
    //Model
    private val courseList = MutableStateFlow(listOf<Course>())
    val courseListReadOnly : StateFlow<List<Course>> = courseList

    // Methods to modify the Model
    fun addCourse (course: Course){
        courseList.value += course
    }

    fun removeCourse (course: Course){
        courseList.value -= course
    }
}

data class Course(var department: String, var courseNumber: Int, var location: String)



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MVVMDemoV2Theme {
                val myVMObj: CourseViewModel = viewModel()
                CourseList (myVMObj)
            }
        }
    }
}

//View
@Composable
fun CourseList(myVM: CourseViewModel) {

    Column(Modifier.fillMaxWidth().statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        // Observe my Courses
        val observableCourses by myVM.courseListReadOnly.collectAsStateWithLifecycle()

        var departmentText by remember { mutableStateOf("") }
        var numText by remember { mutableStateOf("") }
        var locationText by remember { mutableStateOf("") }
        var selectedCourse by remember { mutableStateOf<Course?>(null) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = departmentText,
                onValueChange = { departmentText = it },
                label = { Text("Department") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = numText,
                onValueChange = { numText = it },
                label = { Text("Course Number") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = locationText,
                onValueChange = { locationText = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(8.dp))
        Row {
            Button(onClick = {
                val courseNum = numText.toIntOrNull()
                if (courseNum != null && departmentText.isNotBlank() && locationText.isNotBlank()) {
                    val newCourse = Course(
                        departmentText,
                        courseNum,
                        locationText
                    )

                    myVM.addCourse(newCourse)
                    departmentText = ""
                    numText = ""
                    locationText = ""
                }
            }) {
                Text("Add Item")
            }

        }

        Spacer(Modifier.height(20.dp))
        Text("Course List", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, color = Color.Blue)
        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            // display my list showing only course name (e.g., CS 4530)
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(observableCourses) { course ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedCourse = course },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Text(
                            text = "${course.department} ${course.courseNumber}",
                            fontSize = 20.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(onClick = {
                            myVM.removeCourse(course)
                        }) {
                            Text("Remove")
                        }
                    }
                }
            }
        }

        // Display full details in a dialog when a course is clicked
        selectedCourse?.let { course ->
            AlertDialog(
                onDismissRequest = { selectedCourse = null },
                title = {
                    Text(
                        text = "${course.department} ${course.courseNumber}",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Department: ${course.department}")
                        Text(text = "Course Number: ${course.courseNumber}")
                        Text(text = "Location: ${course.location}")
                    }
                },
                confirmButton = {
                    TextButton(onClick = { selectedCourse = null }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}