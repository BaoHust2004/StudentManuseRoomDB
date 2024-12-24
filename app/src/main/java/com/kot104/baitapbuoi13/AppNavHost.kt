package com.kot104.baitapbuoi13

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kot104.baitapbuoi13.repository.Repository
import com.kot104.baitapbuoi13.room.StudentsDB
import com.kot104.baitapbuoi13.viewmodel.StudentViewModel

enum class ROUTE_NAME_SCREEN {
    Main,
    Detail
}
@Composable
fun AppNavHost() {
    val context = LocalContext.current
    val db = StudentsDB.getIntance(context)
    val repository = Repository(db)
    val myViewModel = StudentViewModel(repository)
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ROUTE_NAME_SCREEN.Main.name){
        composable(ROUTE_NAME_SCREEN.Main.name){ MainScreen(viewModel = myViewModel, navController)}
        composable("${ROUTE_NAME_SCREEN.Detail.name}/{studentId}/{hoTen}/{mssv}/{diemTB}/{daRaTruong}"){
            DetailScreen(
                navController,
                viewModel = myViewModel,
                studentId = it.arguments?.getString("studentId"),
                hoTen = it.arguments?.getString("hoTen")!!,
                mssv = it.arguments?.getString("mssv")!!,
                diemTB = it.arguments?.getString("diemTB")!!,
                daRaTruong = it.arguments?.getString("daRaTruong")!!
            )
        }

    }
}