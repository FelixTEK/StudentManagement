package com.example.studentmanagement.domain.usecase

import com.example.studentmanagement.domain.model.Student
import com.example.studentmanagement.domain.repository.StudentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStudentsUseCase @Inject constructor(
    private val repository: StudentRepository
) {
    operator fun invoke(): Flow<List<Student>> {
        return repository.observeStudents()
    }
}
