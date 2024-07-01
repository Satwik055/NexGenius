package com.satwik.nexgenius.core.main

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MainViewModel:ViewModel() {

    private val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun dismissDialog(){
        visiblePermissionDialogQueue.removeLast()
    }

    fun onPermissionResult(
        permission:String,
        isGranted:Boolean
    ){
        if(!isGranted){
            visiblePermissionDialogQueue.add(0, permission)
        }
    }


}