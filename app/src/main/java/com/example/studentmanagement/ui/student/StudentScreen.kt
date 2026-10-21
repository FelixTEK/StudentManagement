package com.example.studentmanagement.ui.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.studentmanagement.data.local.StudentEntity
import com.example.studentmanagement.worker.SyncScheduler

@Composable
fun StudentScreen(viewModel: StudentViewModel) {
    val context = LocalContext.current
    val students by viewModel.students.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { showAddDialog = true }) {
                Text("+ Add Student")
            }
            Button(onClick = { viewModel.syncStudents() }) {
                Text("Sync Now")
            }
        }
        Button(
            onClick = {
                SyncScheduler.syncNow(context)
            }
        ) {
            Text("Sync in Background")
        }
        LazyColumn {
            items(students) { student ->
                Column {
                    Text(student.name)
                    Text(student.studentCode)
                    Text("Sync: ${student.syncStatus}")
                }
            }
        }
    }

    if (showAddDialog) {
        StudentFormDialog(
            title = "Add Student",
            onDismiss = { showAddDialog = false },
            onSave = { code, name, major ->
                viewModel.addStudent(
                    StudentEntity(studentCode = code, name = name, major = major)
                )
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun StudentFormDialog(
    title: String,
    onDismiss: () -> Unit,
    onSave: (code: String, name: String, major: String) -> Unit
) {
    var codeInput by remember { mutableStateOf("") }
    var nameInput by remember { mutableStateOf("") }
    var majorInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = codeInput,
                    onValueChange = { codeInput = it },
                    label = { Text("Student Code") }
                )
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Name") }
                )
                OutlinedTextField(
                    value = majorInput,
                    onValueChange = { majorInput = it },
                    label = { Text("Major") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (codeInput.isNotBlank() && nameInput.isNotBlank()) {
                    onSave(codeInput, nameInput, majorInput)
                }
            }) {
                Text("SAVE")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
