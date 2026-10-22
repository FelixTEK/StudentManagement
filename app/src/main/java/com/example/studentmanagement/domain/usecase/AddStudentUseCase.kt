package com.example.studentmanagement.domain.usecase

import com.example.studentmanagement.domain.model.Student
import com.example.studentmanagement.domain.repository.StudentRepository
import javax.inject.Inject

class AddStudentUseCase @Inject constructor(
    private val repository: StudentRepository
) {
    suspend operator fun invoke(student: Student) {
        repository.addStudent(student)
    }
}
