package com.example.studentmanagement.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StudentApi {
    @GET("students")
    suspend fun getStudents(): List<StudentDto>

    @POST("students")
    suspend fun createStudent(
        @Body student: StudentCreateRequest
    ): StudentDto

    @PUT("students/{id}")
    suspend fun updateStudent(
        @Path("id") id: Int,
        @Body student: StudentCreateRequest
    ): StudentDto

    @DELETE("students/{id}")
    suspend fun deleteStudent(
        @Path("id") id: Int
    )
}

object RetrofitClient {
    private const val BASE_URL =
        "http://127.0.0.1:8000/"
    val api: StudentApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(StudentApi::class.java)
    }
}

