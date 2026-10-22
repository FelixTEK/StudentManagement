package com.example.studentmanagement.domain.model

data class Student(
    val id: Int,
    val studentCode: String,
    val name: String,
    val major: String,
    val syncStatus: String = "PENDING"
)
