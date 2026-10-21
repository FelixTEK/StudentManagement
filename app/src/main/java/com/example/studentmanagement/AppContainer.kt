package com.example.studentmanagement

import android.content.Context
import androidx.room.Room
import com.example.studentmanagement.data.local.AppDatabase
import com.example.studentmanagement.data.remote.RetrofitClient
import com.example.studentmanagement.data.remote.StudentApi
import com.example.studentmanagement.data.remote.TokenStorage
import com.example.studentmanagement.data.repository.StudentRepository

class AppContainer(context: Context) {

    private val database = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "student.db"
    ).build()

    private val tokenStorage = TokenStorage(context)

    private val retrofit = RetrofitClient.create(tokenStorage)

    private val studentApi = retrofit.create(StudentApi::class.java)

    val studentRepository = StudentRepository(
        dao = database.studentDao(),
        api = studentApi
    )
}
