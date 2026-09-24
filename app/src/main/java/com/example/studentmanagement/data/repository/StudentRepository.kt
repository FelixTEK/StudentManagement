package com.example.studentmanagement.data.repository

import com.example.studentmanagement.data.local.StudentDao
import com.example.studentmanagement.model.Student

class StudentRepository(
    private val dao: StudentDao
) {
    suspend fun getStudents(): List<Student> = dao.getAllStudents()

    suspend fun addStudent(student: Student) {
        dao.insertStudent(student)
    }

    suspend fun deleteStudent(student: Student) {
        dao.deleteStudent(student)
    }
}