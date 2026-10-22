package com.example.studentmanagement.ui.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentmanagement.domain.model.Student
import com.example.studentmanagement.domain.usecase.AddStudentUseCase
import com.example.studentmanagement.domain.usecase.GetStudentsUseCase
import com.example.studentmanagement.domain.usecase.SyncStudentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentViewModel @Inject constructor(
    private val getStudentsUseCase: GetStudentsUseCase,
    private val addStudentUseCase: AddStudentUseCase,
    private val syncStudentsUseCase: SyncStudentsUseCase
) : ViewModel() {

    val students =
        getStudentsUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun addStudent(student: Student) {
        viewModelScope.launch {
            addStudentUseCase(student)
        }
    }

    fun syncStudents() {
        viewModelScope.launch {
            syncStudentsUseCase()
        }
    }
}
