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

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var searchQuery by mutableStateOf("")
        private set

    val filteredStudents: List<Student>
        get() = if (searchQuery.isBlank()) {
            students
        } else {
            students.filter {
                it.studentCode.contains(searchQuery, ignoreCase = true) ||
                    it.name.contains(searchQuery, ignoreCase = true) ||
                    it.major.contains(searchQuery, ignoreCase = true)
            }
        }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }

    fun loadStudents() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                students = repository.getStudents()
            } catch (e: Exception) {
                errorMessage = "ไม่สามารถโหลดข้อมูลได้"
            } finally {
                isLoading = false
            }
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

    fun updateStudent(student: Student) {
        viewModelScope.launch {
            repository.addStudent(student)
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