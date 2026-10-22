package com.example.studentmanagement.domain.usecase

import com.example.studentmanagement.domain.repository.StudentRepository
import javax.inject.Inject

class SyncStudentsUseCase @Inject constructor(
    private val repository: StudentRepository
) {
    suspend operator fun invoke() {
        repository.syncPending()
    }
}
