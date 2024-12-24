package com.kot104.baitapbuoi13

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kot104.baitapbuoi13.room.StudentEntity
import com.kot104.baitapbuoi13.viewmodel.StudentViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: StudentViewModel, navController: NavController) {
    var inputHoten by remember { mutableStateOf("") }
    var inputMssv by remember { mutableStateOf("") }
    val emty by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Search states
    var searchName by remember { mutableStateOf("") }
    var searchMssv by remember { mutableStateOf("") }

    // Selection states
    var selectedStudents by remember { mutableStateOf(setOf<Int>()) }
    var isSelectionMode by remember { mutableStateOf(false) }

    val students by viewModel.students.collectAsState(initial = emptyList())

    // Filter students based on search criteria
    val filteredStudents = students.filter { student ->
        (student.hoten?.contains(searchName, ignoreCase = true) ?: false) &&
                (student.mssv?.contains(searchMssv, ignoreCase = true) ?: false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isSelectionMode) "${selectedStudents.size} Selected" else "Student name",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.background
                    )
                },
                actions = {
                    if (isSelectionMode) {
                        IconButton(
                            onClick = {
                                if (selectedStudents.isNotEmpty()) {
                                    showDeleteDialog = true
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_delete),
                                contentDescription = "Delete selected",
                                tint = MaterialTheme.colorScheme.background
                            )
                        }
                        IconButton(
                            onClick = {
                                isSelectionMode = false
                                selectedStudents = emptySet()
                            }
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Cancel selection",
                                tint = MaterialTheme.colorScheme.background
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary)
            )
        },
        floatingActionButton = {
            if (!isSelectionMode) {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background,
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Search fields
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    OutlinedTextField(
                        value = searchName,
                        onValueChange = { searchName = it },
                        label = { Text("Tìm theo tên") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        singleLine = true,
                        trailingIcon = {
                            if (searchName.isNotEmpty()) {
                                IconButton(onClick = { searchName = "" }) {
                                    Icon(Icons.Default.Close, "Clear search")
                                }
                            }
                        }
                    )

                    OutlinedTextField(
                        value = searchMssv,
                        onValueChange = { searchMssv = it },
                        label = { Text("Tìm theo MSSV") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        trailingIcon = {
                            if (searchMssv.isNotEmpty()) {
                                IconButton(onClick = { searchMssv = "" }) {
                                    Icon(Icons.Default.Close, "Clear search")
                                }
                            }
                        }
                    )
                }
            }

            // Student list
            if (filteredStudents.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Text(
                        text = if (students.isEmpty()) "No data" else "No matching results",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    items(filteredStudents) { student ->
                        Card(
                            onClick = {
                                if (isSelectionMode) {
                                    selectedStudents = if (selectedStudents.contains(student.uid)) {
                                        selectedStudents - student.uid
                                    } else {
                                        selectedStudents + student.uid
                                    }
                                } else {
                                    navController.navigate(
                                        "${ROUTE_NAME_SCREEN.Detail.name}/${Uri.encode(student.uid.toString())}/${
                                            Uri.encode(student.hoten ?: "")
                                        }/${Uri.encode(student.mssv ?: "")}/${Uri.encode("10")}/${Uri.encode("true")}"
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.fillMaxWidth(0.85f)) {
                                    Text(
                                        text = "Họ tên: " + (student.hoten ?: ""),
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(5.dp),
                                    )
                                    Text(
                                        text = "MSSV: " + (student.mssv ?: ""),
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(5.dp),
                                    )
                                }

                                if (isSelectionMode) {
                                    Checkbox(
                                        checked = selectedStudents.contains(student.uid),
                                        onCheckedChange = { checked ->
                                            selectedStudents = if (checked) {
                                                selectedStudents + student.uid
                                            } else {
                                                selectedStudents - student.uid
                                            }
                                        }
                                    )
                                } else {
                                    IconButton(
                                        onClick = {
                                            isSelectionMode = true
                                            selectedStudents = setOf(student.uid)
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.MoreVert,
                                            contentDescription = "Select"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                        inputHoten = emty
                        inputMssv = emty
                    }
                ) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                if (inputHoten.isNotEmpty() && inputMssv.isNotEmpty()) {
                    Button(
                        onClick = {
                            viewModel.addStudent(
                                StudentEntity(
                                    0,
                                    inputHoten,
                                    inputMssv,
                                    10f,
                                    true
                                )
                            )
                            showDialog = false
                            inputHoten = emty
                            inputMssv = emty
                        }
                    ) {
                        Text(text = "Save")
                    }
                }
            },
            title = {
                Text(
                    text = "Add Student",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(5.dp)
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = inputHoten,
                        onValueChange = { inputHoten = it },
                        label = { Text(text = "Họ Tên") },
                        placeholder = { Text(text = "Nhập Họ Tên") }
                    )
                    OutlinedTextField(
                        value = inputMssv,
                        onValueChange = { inputMssv = it },
                        label = { Text(text = "MSSV") },
                        placeholder = { Text(text = "Nhập MSSV") }
                    )
                }
            }
        )
    }

    // Delete Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Selected Students") },
            text = { Text("Are you sure you want to delete ${selectedStudents.size} selected students?") },
            confirmButton = {
                Button(
                    onClick = {
                        students.filter { selectedStudents.contains(it.uid) }.forEach { student ->
                            viewModel.deleteStudent(student)
                        }
                        selectedStudents = emptySet()
                        isSelectionMode = false
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}