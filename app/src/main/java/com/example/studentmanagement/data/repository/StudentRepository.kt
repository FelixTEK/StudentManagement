package com.example.studentmanagement.data.repository

import com.example.studentmanagement.data.local.StudentDao
import com.example.studentmanagement.data.remote.StudentApi
import com.example.studentmanagement.model.Student

class StudentRepository(
    private val dao: StudentDao,
    private val api: StudentApi
) {
    suspend fun getStudents(): List<Student> {
        return try {
            val remoteStudents = api.getStudents()
            remoteStudents.forEach { dao.insertStudent(it) }
            remoteStudents
        } catch (e: Exception) {
            dao.getAllStudents()
        }
    }

    suspend fun addStudent(student: Student) {
        dao.insertStudent(student)
    }

    suspend fun deleteStudent(student: Student) {
        dao.deleteStudent(student)
    }
}