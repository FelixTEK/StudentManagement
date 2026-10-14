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
        // id ให้ server เป็นผู้สร้าง ส่งค่า 0 ไปเป็นแค่ placeholder (ไม่ได้ถูกใช้)
        val newStudent = Student(id = 0, studentCode = code, name = name, major = major)
        runAndReload("เพิ่มนักศึกษาไม่สำเร็จ") { repository.addStudent(newStudent) }
    }

    fun updateStudent(student: Student) {
        runAndReload("แก้ไขนักศึกษาไม่สำเร็จ") { repository.updateStudent(student) }
    }

    fun deleteStudent(student: Student) {
        runAndReload("ลบนักศึกษาไม่สำเร็จ") { repository.deleteStudent(student) }
    }

    private fun runAndReload(failMessage: String, action: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                action()
                loadStudents()
            } catch (e: Exception) {
                errorMessage = failMessage
            }
        }
    }
}