package com.example.studentmanagement.data.remote

import com.example.studentmanagement.model.Student
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface StudentApi {
    @GET("users")
    suspend fun getStudents():
            List<Student>
}

object RetrofitClient {
    private const val BASE_URL =
        "https://example.com/api/"
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

