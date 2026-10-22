package com.example.studentmanagement.data.repository

import com.example.studentmanagement.data.local.StudentDao
import com.example.studentmanagement.data.local.StudentEntity
import com.example.studentmanagement.data.remote.StudentApi
import com.example.studentmanagement.data.remote.StudentCreateRequest
import com.example.studentmanagement.domain.model.Student
import com.example.studentmanagement.domain.repository.StudentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StudentRepositoryImpl @Inject constructor(
    private val dao: StudentDao,
    private val api: StudentApi
) : StudentRepository {

    override fun observeStudents(): Flow<List<Student>> {
        return dao.observeStudents()
            .map { entities ->
                entities.map { entity ->
                    Student(
                        id = entity.serverId ?: entity.localId,
                        studentCode = entity.studentCode,
                        name = entity.name,
                        major = entity.major,
                        syncStatus = entity.syncStatus
                    )
                }
            }
    }

    override suspend fun addStudent(student: Student) {
        dao.insertStudent(
            StudentEntity(
                studentCode = student.studentCode,
                name = student.name,
                major = student.major,
                syncStatus = "PENDING"
            )
        )
    }

    override suspend fun syncPending() {
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
