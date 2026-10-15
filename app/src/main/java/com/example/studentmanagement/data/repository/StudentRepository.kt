package com.example.studentmanagement.data.repository

import com.example.studentmanagement.data.local.StudentDao
import com.example.studentmanagement.data.remote.StudentApi
import com.example.studentmanagement.data.remote.StudentCreateRequest
import com.example.studentmanagement.data.remote.StudentDto
import com.example.studentmanagement.model.Student

class StudentRepository(
    private val dao: StudentDao,
    private val api: StudentApi
) {
    suspend fun getStudents(): List<Student> {
        return try {
            val remoteStudents = api.getStudents().map {
                Student(
                    id = it.id,
                    studentCode = it.studentCode,
                    name = it.name,
                    major = it.major
                )
            }
            dao.deleteAll()
            dao.insertAll(remoteStudents)
            remoteStudents
        } catch (e: Exception) {
            dao.getAllStudents()
        }
    }

    suspend fun addStudent(student: Student) {
        val created = api.createStudent(student.toRequest())
        dao.insertStudent(created.toStudent())
    }

    suspend fun updateStudent(student: Student) {
        val updated = api.updateStudent(student.id, student.toRequest())
        dao.insertStudent(updated.toStudent())
    }

    suspend fun deleteStudent(student: Student) {
        api.deleteStudent(student.id)
        dao.deleteStudent(student)
    }

    private fun Student.toRequest() = StudentCreateRequest(
        studentCode = studentCode,
        name = name,
        major = major
    )

    private fun StudentDto.toStudent() = Student(
        id = id,
        studentCode = studentCode,
        name = name,
        major = major
    )
}