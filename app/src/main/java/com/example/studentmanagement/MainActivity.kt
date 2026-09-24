package com.example.studentmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.studentmanagement.data.local.AppDatabase
import com.example.studentmanagement.data.repository.StudentRepository
import com.example.studentmanagement.ui.StudentScreen
import com.example.studentmanagement.ui.theme.StudentManagementTheme
import com.example.studentmanagement.viewmodel.StudentViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "student_database"
        ).build()

        val repository = StudentRepository(db.studentDao())

        val viewModel = StudentViewModel(repository)

        setContent {
            StudentManagementTheme {
                StudentScreen(viewModel = viewModel)
            }
        }
    }
}