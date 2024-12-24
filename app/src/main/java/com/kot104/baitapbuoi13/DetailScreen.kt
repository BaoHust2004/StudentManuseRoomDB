package com.kot104.baitapbuoi13

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: StudentViewModel,
    studentId: String?,
    hoTen: String,
    mssv: String,
    diemTB: String,
    daRaTruong: String
) {
    val showDialog = remember { mutableStateOf(false) }
    val showUpdateDialog = remember { mutableStateOf(false) }
    var inputHoten by remember { mutableStateOf(hoTen) }
    var inputMssv by remember { mutableStateOf(mssv) }
    var inputDiemTB by remember { mutableStateOf(diemTB) }
    var inputDaRaTruong by remember { mutableStateOf(daRaTruong) }
    val emty by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().safeDrawingPadding()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(15.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Row {
                    Text(
                        text = "ID: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "" + studentId,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Row {
                    Text(
                        text = "Họ Tên: ",
                        fontSize = 16.sp,
                    )
                    Text(
                        text = "" + hoTen,
                        fontSize = 16.sp,
                    )
                }
                Row {
                    Text(
                        text = "MSSV: ",
                        fontSize = 16.sp,
                    )
                    Text(
                        text = "" + mssv,
                        fontSize = 16.sp,
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.background)
                ) {

                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    OutlinedIconButton(
                        onClick = { showDialog.value = true },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.size(height = 50.dp, width = 100.dp)
                    ) {
                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_delete),
                                contentDescription = null
                            )
                            Text(text = "Delete")
                        }
                    }
                    OutlinedIconButton(
                        onClick = {showUpdateDialog.value = true},
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.size(height = 50.dp, width = 100.dp)
                    ) {
                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_edit),
                                contentDescription = null
                            )
                            Text(text = "Update")
                        }
                    }
                }
            }
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                        navController.popBackStack()
                    }
                ) {
                    Text(text = "No")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (studentId != null) {
                            viewModel.deleteStudent(
                                student = StudentEntity(
                                    uid = studentId.toInt(),
                                    hoten = hoTen,
                                    mssv = mssv,
                                    diemTB = diemTB?.toFloat(),
                                    daratruong = daRaTruong?.toBoolean()
                                )
                            )
                        }
                        showDialog.value = false
                        navController.popBackStack()
                    }
                ) {
                    Text(text = "Yes")
                }
            },
            title = {
                Text(
                    text = "Delete student",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
            },
            text = {
                Text(
                    text = "Are you sure?",
                    fontSize = 25.sp
                )
            }
        )
    }

    if (showUpdateDialog.value) {
        AlertDialog(
            onDismissRequest = { showUpdateDialog.value = false },
            dismissButton = {
                Button(
                    onClick = {
                        showUpdateDialog.value = false
                        inputHoten = emty
                        inputMssv = emty
                        inputDiemTB = emty
                        inputDaRaTruong = emty

                    }
                ) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                if (inputHoten.isNotEmpty() || inputMssv.isNotEmpty() || inputDiemTB.isNotEmpty() || inputDaRaTruong.isNotEmpty()) {

                    Button(
                        onClick = {
                            val newStudent = StudentEntity(studentId!!.toInt(), inputHoten, inputMssv, inputDiemTB.toFloat(), inputDaRaTruong.toBoolean())
                            viewModel.updateStudent(newStudent)
                            navController.popBackStack()
                            showUpdateDialog.value = false
                            inputHoten = emty
                            inputMssv = emty
                            inputDiemTB = emty
                            inputDaRaTruong = emty
                        }
                    ) {
                        Text(text = "Update")
                    }
                }
            },
            title = {
                Text(
                    text = "Update Student",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(5.dp)
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = inputHoten,
                        onValueChange = {inputHoten = it},
                        label = {
                            Text(text = "Họ Tên")
                        },
                        placeholder = { Text(text = "Nhập Họ Tên")}
                    )
                    OutlinedTextField(
                        value = inputMssv,
                        onValueChange = {inputMssv = it},
                        label = {
                            Text(text = "MSSV")
                        },
                        placeholder = { Text(text = "Nhập MSSV")}
                    )

                }
            }
        )
    }
}