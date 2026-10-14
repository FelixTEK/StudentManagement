package com.example.studentmanagement.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentmanagement.model.Student
import com.example.studentmanagement.viewmodel.StudentViewModel

@Composable
fun StudentScreen(viewModel: StudentViewModel) {
    var selectedStudent by remember { mutableStateOf<Student?>(null) }

    val student = selectedStudent
    if (student == null) {
        StudentListScreen(
            viewModel = viewModel,
            onStudentClick = { selectedStudent = it }
        )
    } else {
        StudentDetailScreen(
            student = student,
            onBack = { selectedStudent = null },
            onEdit = { updated ->
                viewModel.updateStudent(updated)
                selectedStudent = updated
            },
            onDelete = {
                viewModel.deleteStudent(student)
                selectedStudent = null
            }
        )
    }
}

@Composable
private fun StudentListScreen(
    viewModel: StudentViewModel,
    onStudentClick: (Student) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadStudents()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Student Management", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.loadStudents() }) {
                Text("Refresh")
            }
            Button(onClick = { showAddDialog = true }) {
                Text("+ Add Student")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = viewModel.searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("🔍 Search student...") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        viewModel.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(viewModel.filteredStudents) { student ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onStudentClick(student) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "${student.studentCode} - ${student.name}", style = MaterialTheme.typography.titleMedium)
                                Text(text = student.major, style = MaterialTheme.typography.bodyMedium)
                            }

                            Button(
                                onClick = { viewModel.deleteStudent(student) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        StudentFormDialog(
            title = "Add Student",
            initial = null,
            onDismiss = { showAddDialog = false },
            onSave = { code, name, major ->
                viewModel.addStudent(code, name, major)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun StudentDetailScreen(
    student: Student,
    onBack: () -> Unit,
    onEdit: (Student) -> Unit,
    onDelete: () -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("< Back")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Student Detail", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("ID     : ${student.id}")
                Text("Code   : ${student.studentCode}")
                Text("Name   : ${student.name}")
                Text("Major  : ${student.major}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { showEditDialog = true }) {
                Text("Edit")
            }
            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete")
            }
        }
    }

    if (showEditDialog) {
        StudentFormDialog(
            title = "Edit Student",
            initial = student,
            onDismiss = { showEditDialog = false },
            onSave = { code, name, major ->
                onEdit(student.copy(studentCode = code, name = name, major = major))
                showEditDialog = false
            }
        )
    }
}

@Composable
private fun StudentFormDialog(
    title: String,
    initial: Student?,
    onDismiss: () -> Unit,
    onSave: (code: String, name: String, major: String) -> Unit
) {
    var codeInput by remember { mutableStateOf(initial?.studentCode ?: "") }
    var nameInput by remember { mutableStateOf(initial?.name ?: "") }
    var majorInput by remember { mutableStateOf(initial?.major ?: "") }

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
