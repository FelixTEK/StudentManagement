package com.example.studentmanagement.domain.usecase

import com.example.studentmanagement.domain.model.Student
import com.example.studentmanagement.domain.repository.StudentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FakeStudentRepository : StudentRepository {

    private val students = MutableStateFlow(
        listOf(
            Student(
                id = 1,
                studentCode = "66001",
                name = "สมชาย",
                major = "IT"
            )
        )
    )

    override fun observeStudents(): Flow<List<Student>> {
        return students
    }

    override suspend fun addStudent(student: Student) {
        students.value = students.value + student
    }

    override suspend fun syncPending() {
    }
}

class GetStudentsUseCaseTest {

    @Test
    fun `get students returns student list`() = runTest {
        val repository = FakeStudentRepository()
        val useCase = GetStudentsUseCase(repository)

        val result = useCase().first()

        assertEquals(1, result.size)
        assertEquals("สมชาย", result.first().name)
    }
}
