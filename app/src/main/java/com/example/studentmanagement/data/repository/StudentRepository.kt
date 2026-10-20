package com.example.studentmanagement.data.repository

import com.example.studentmanagement.data.local.StudentDao
import com.example.studentmanagement.data.local.StudentEntity
import com.example.studentmanagement.data.remote.StudentApi
import com.example.studentmanagement.data.remote.StudentCreateRequest
import kotlinx.coroutines.flow.Flow

class StudentRepository(
    private val dao: StudentDao,
    private val api: StudentApi
) {
    fun observeStudents(): Flow<List<StudentEntity>> {
        return dao.observeStudents()
    }

    suspend fun addStudent(student: StudentEntity) {
        dao.insertStudent(
            student.copy(syncStatus = "PENDING")
        )
    }

    suspend fun syncPending() {
        val pending = dao.getPendingStudents()

        for (student in pending) {
            val response = api.createStudent(
                StudentCreateRequest(
                    studentCode = student.studentCode,
                    name = student.name,
                    major = student.major
                )
            )

            dao.updateStudent(
                student.copy(
                    serverId = response.id,
                    syncStatus = "SYNCED"
                )
            )
        }
    }
}
