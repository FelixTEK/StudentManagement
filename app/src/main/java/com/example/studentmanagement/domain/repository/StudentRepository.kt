package com.example.studentmanagement.domain.repository

import com.example.studentmanagement.domain.model.Student
import kotlinx.coroutines.flow.Flow

interface StudentRepository {
    fun observeStudents(): Flow<List<Student>>

    suspend fun addStudent(student: Student)

    suspend fun syncPending()
}
