package com.example.studentmanagement.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface StudentApi {
    @GET("students")
    suspend fun getStudents(): List<StudentDto>

    @POST("students")
    suspend fun createStudent(
        @Body student: StudentCreateRequest
    ): StudentDto
}
