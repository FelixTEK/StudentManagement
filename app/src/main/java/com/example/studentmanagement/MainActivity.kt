package com.example.studentmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.room.Room
import com.example.studentmanagement.data.local.AppDatabase
import com.example.studentmanagement.data.remote.AuthApi
import com.example.studentmanagement.data.remote.RetrofitClient
import com.example.studentmanagement.data.remote.StudentApi
import com.example.studentmanagement.data.remote.TokenStorage
import com.example.studentmanagement.data.repository.StudentRepository
import com.example.studentmanagement.ui.login.LoginScreen
import com.example.studentmanagement.ui.login.LoginViewModel
import com.example.studentmanagement.ui.student.StudentScreen
import com.example.studentmanagement.ui.student.StudentViewModel
import com.example.studentmanagement.ui.theme.StudentManagementTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "student_database"
        ).build()

        val tokenStorage = TokenStorage(applicationContext)
        val retrofit = RetrofitClient.create(tokenStorage)
        val studentApi = retrofit.create(StudentApi::class.java)
        val authApi = retrofit.create(AuthApi::class.java)

        val repository = StudentRepository(db.studentDao(), studentApi)

        val viewModel = StudentViewModel(repository)
        val loginViewModel = LoginViewModel(authApi, tokenStorage)

        setContent {
            StudentManagementTheme {
                var isLoggedIn by remember {
                    mutableStateOf(tokenStorage.getToken() != null)
                }

                if (isLoggedIn) {
                    StudentScreen(viewModel = viewModel)
                } else {
                    LoginScreen(
                        viewModel = loginViewModel,
                        onLoginSuccess = { isLoggedIn = true }
                    )
                }
            }
        }
    }
}
