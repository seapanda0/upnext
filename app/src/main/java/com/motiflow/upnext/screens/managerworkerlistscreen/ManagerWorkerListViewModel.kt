package com.motiflow.upnext.screens.managerworkerlistscreen

import androidx.lifecycle.ViewModel
import com.motiflow.upnext.Routes
import com.motiflow.upnext.model.DataRepoService

class ManagerWorkerListViewModel(): ViewModel() {
    val workers = DataRepoService.allWorkers

    fun onClickWorker(navigateTo: (String) -> Unit ,workerId: String){
        navigateTo(Routes.WORKER_TODO_LIST_SCREEN + "?workerId=${workerId}")
    }
}