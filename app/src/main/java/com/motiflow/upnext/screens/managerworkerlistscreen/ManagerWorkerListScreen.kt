package com.motiflow.upnext.screens.managerworkerlistscreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.motiflow.upnext.screens.workertodolistscreen.WorkerTodoListViewModel

@Composable
fun ManagerWorkerListScreen(
    navigateTo: (String) -> Unit,
    viewModel: ManagerWorkerListViewModel = viewModel()
) {
    Text("manager worker list screen")
}