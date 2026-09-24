package com.example.studentmanagement.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentmanagement.data.repository.StudentRepository
import com.example.studentmanagement.model.Student
import kotlinx.coroutines.launch

class StudentViewModel(
    private val repository: StudentRepository
) : ViewModel() {

    var students by mutableStateOf<List<Student>>(emptyList())
        private set

    fun loadStudents() {
        viewModelScope.launch {
            students = repository.getStudents()
        }
    }

    fun addStudent(code: String, name: String, major: String) {
        viewModelScope.launch {
            val newId = (students.maxOfOrNull { it.id } ?: 0) + 1
            val newStudent = Student(id = newId, studentCode = code, name = name, major = major)
            repository.addStudent(newStudent)
            loadStudents()
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            repository.deleteStudent(student)
            loadStudents()
        }
    }
}