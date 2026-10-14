package com.example.studentmanagement.data.remote

import com.google.gson.annotations.SerializedName

data class StudentDto(
    val id: Int,
    @SerializedName("student_code") val studentCode: String,
    val name: String,
    val major: String
)
