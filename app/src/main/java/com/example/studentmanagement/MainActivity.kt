package com.example.studentmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.studentmanagement.data.remote.AuthApi
import com.example.studentmanagement.data.remote.RetrofitClient
import com.example.studentmanagement.data.remote.TokenStorage
import com.example.studentmanagement.ui.login.LoginScreen
import com.example.studentmanagement.ui.login.LoginViewModel
import com.example.studentmanagement.ui.student.StudentScreen
import com.example.studentmanagement.ui.student.StudentViewModel
import com.example.studentmanagement.ui.theme.StudentManagementTheme
import com.example.studentmanagement.worker.SyncScheduler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenStorage = TokenStorage(applicationContext)
        val retrofit = RetrofitClient.create(tokenStorage)
        val authApi = retrofit.create(AuthApi::class.java)

        val repository = (application as StudentApplication).appContainer.studentRepository

        val viewModel = StudentViewModel(repository)
        val loginViewModel = LoginViewModel(authApi, tokenStorage)

        SyncScheduler.schedulePeriodicSync(this)

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
